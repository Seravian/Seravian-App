package com.seravian.feat_chat.data

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.AutomaticGainControl
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.sqrt

class AudioStreamer(
    private val scope: CoroutineScope,
    private val onCapturingComplete: suspend (ByteArray) -> Unit,
    private val onAmplitudeUpdate: (Float) -> Unit,
    private val silenceThreshold: Int = 2000,
    private val voiceThreshold: Int = 530,           // Adjustable voice detection threshold
    private val minRecordingDuration: Int = 500,     // Minimum ms of audio before processing
    private val audioGain: Float = 1.0f              // Audio gain multiplier
) {
    private val sampleRate = 16000
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private lateinit var recorder: AudioRecord
    private var noiseSuppressor: NoiseSuppressor? = null
    private var acousticEchoCanceler: AcousticEchoCanceler? = null
    private var automaticGainControl: AutomaticGainControl? = null

    private var isRecording = false
    private var recordingJob: Job? = null

    private val audioBuffer = ByteArrayOutputStream()
    private var recordingStartTime = 0L

    @SuppressLint("MissingPermission")
    fun start() {
        if (isRecording) return

        audioBuffer.reset()
        recordingStartTime = System.currentTimeMillis()

        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        // Apply all available audio enhancements
        if (NoiseSuppressor.isAvailable()) {
            noiseSuppressor = NoiseSuppressor.create(recorder.audioSessionId).apply {
                enabled = true
            }
        }

        if (AcousticEchoCanceler.isAvailable()) {
            acousticEchoCanceler = AcousticEchoCanceler.create(recorder.audioSessionId).apply {
                enabled = true
            }
        }

        if (AutomaticGainControl.isAvailable()) {
            automaticGainControl = AutomaticGainControl.create(recorder.audioSessionId).apply {
                enabled = true
            }
        }

        isRecording = true
        recorder.startRecording()

        recordingJob = scope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            var lastVoiceTime = System.currentTimeMillis()
            var voiceDetected = false

            // For adaptive threshold
            val rmsHistory = mutableListOf<Double>()
            var adaptiveThreshold = voiceThreshold.toDouble()

            // Background noise level detection
            val initialSamples = 5
            val backgroundNoiseSamples = mutableListOf<Double>()

            while (isRecording && recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    // Apply gain if needed
                    val processedChunk = if (audioGain != 1.0f) {
                        applyGain(buffer.copyOf(read), audioGain)
                    } else {
                        buffer.copyOf(read)
                    }

                    audioBuffer.write(processedChunk, 0, processedChunk.size)

                    val rms = calculateRMS(processedChunk)
                    onAmplitudeUpdate(rms.toFloat())

                    // Collect background noise level during first few samples
                    if (backgroundNoiseSamples.size < initialSamples) {
                        backgroundNoiseSamples.add(rms)
                        continue
                    } else if (backgroundNoiseSamples.size == initialSamples) {
                        // Calculate noise floor and adjust adaptive threshold
                        val noiseFloor = backgroundNoiseSamples.average()
                        adaptiveThreshold = max(voiceThreshold.toDouble(), noiseFloor * 2.5)
                        backgroundNoiseSamples.add(0.0) // Add dummy value to prevent recalculation
                    }

                    // Add to RMS history and maintain reasonable size
                    rmsHistory.add(rms)
                    if (rmsHistory.size > 10) rmsHistory.removeAt(0)

                    val currentTime = System.currentTimeMillis()

                    // Check if the current RMS exceeds the threshold
                    if (rms > adaptiveThreshold) {
                        lastVoiceTime = currentTime
                        voiceDetected = true
                    }

                    // Check if we've stopped talking and recording meets minimum duration
                    val recordingDuration = currentTime - recordingStartTime
                    if (voiceDetected &&
                        currentTime - lastVoiceTime > silenceThreshold &&
                        recordingDuration > minRecordingDuration) {

                        withContext(Dispatchers.IO) {
                            val completeAudio = audioBuffer.toByteArray()
                            if (completeAudio.isNotEmpty()) {
                                // Optional: noise gate post-processing could be added here
                                onCapturingComplete(completeAudio)
                            }
                        }
                        stop()
                    }
                }
            }
        }
    }

    fun stop() {
        if (!isRecording) return

        isRecording = false

        recordingJob?.cancel()
        recordingJob = null

        cleanupAudioResources()
    }

    private fun cleanupAudioResources() {
        try {
            if (recorder.state == AudioRecord.STATE_INITIALIZED) {
                recorder.stop()
            }
            recorder.release()

            noiseSuppressor?.release()
            noiseSuppressor = null

            acousticEchoCanceler?.release()
            acousticEchoCanceler = null

            automaticGainControl?.release()
            automaticGainControl = null

            audioBuffer.reset()
        } catch (e: Exception) {
            Log.e("AudioStreamer", "Error cleaning up audio resources", e)
        }
    }

    private fun calculateRMS(buffer: ByteArray): Double {
        var sum = 0.0
        val shortBuffer = ShortArray(buffer.size / 2)
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)

        for (sample in shortBuffer) {
            sum += sample * sample
        }

        return sqrt(sum / shortBuffer.size)
    }

    private fun applyGain(buffer: ByteArray, gain: Float): ByteArray {
        val shortBuffer = ShortArray(buffer.size / 2)
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)

        for (i in shortBuffer.indices) {
            // Apply gain with clipping protection
            val sample = shortBuffer[i] * gain
            shortBuffer[i] = when {
                sample > Short.MAX_VALUE -> Short.MAX_VALUE
                sample < Short.MIN_VALUE -> Short.MIN_VALUE
                else -> sample.toInt().toShort()
            }
        }

        val result = ByteArray(buffer.size)
        ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortBuffer)
        return result
    }
}