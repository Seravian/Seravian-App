package com.seravian.feat_chat.data

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
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
import kotlin.math.sqrt

class AudioStreamer(
    private val scope: CoroutineScope,
    private val onCapturingComplete: suspend (ByteArray) -> Unit,
    private val onAmplitudeUpdate: (Float) -> Unit,
    private val silenceThreshold: Int = 2000
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

    private var isRecording = false
    private var recordingJob: Job? = null

    private val audioBuffer = ByteArrayOutputStream()

    @SuppressLint("MissingPermission")
    fun start() {
        if (isRecording) return

        audioBuffer.reset()

        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION, // Better for voice capture
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

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

        isRecording = true
        recorder.startRecording()

        recordingJob = scope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            var lastVoiceTime = System.currentTimeMillis()
            var voiceDetected = false

            while (isRecording && recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val chunk = buffer.copyOf(read)

                    audioBuffer.write(chunk, 0, read)

                    val rms = calculateRMS(chunk)
                    onAmplitudeUpdate(rms.toFloat())

                    val currentTime = System.currentTimeMillis()

                    if (rms > 250) {
                        lastVoiceTime = currentTime
                        voiceDetected = true
                    }

                    if (voiceDetected && currentTime - lastVoiceTime > silenceThreshold) {
                        withContext(Dispatchers.IO) {
                            val completeAudio = audioBuffer.toByteArray()
                            if (completeAudio.isNotEmpty()) {
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
}