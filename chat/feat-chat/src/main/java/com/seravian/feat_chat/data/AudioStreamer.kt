package com.seravian.feat_chat.data

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaFormat
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
    private val silenceThreshold: Int = 2500,
    private val voiceThreshold: Int = 500,
    private val minRecordingDuration: Int = 500,
    private val audioGain: Float = 1.0f
) {
    private val sampleRate = 16000
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private lateinit var recorder: AudioRecord
    private lateinit var flacEncoder: MediaCodec
    private var isRecording = false
    private var recordingJob: Job? = null

    private val encodedFlac = ByteArrayOutputStream()
    private var recordingStartTime = 0L

    @SuppressLint("MissingPermission")
    fun start() {
        if (isRecording) return

        recordingStartTime = System.currentTimeMillis()
        encodedFlac.reset()

        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        setupFlacEncoder()

        recorder.startRecording()
        isRecording = true

        recordingJob = scope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            var lastVoiceTime = System.currentTimeMillis()
            var voiceDetected = false

            val rmsHistory = mutableListOf<Double>()
            var adaptiveThreshold = voiceThreshold.toDouble()

            val backgroundNoiseSamples = mutableListOf<Double>()

            while (isRecording && recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val processedChunk = if (audioGain != 1.0f)
                        applyGain(buffer.copyOf(read), audioGain)
                    else
                        buffer.copyOf(read)

                    val rms = calculateRMS(processedChunk)
                    onAmplitudeUpdate(rms.toFloat())

                    if (backgroundNoiseSamples.size < 5) {
                        backgroundNoiseSamples.add(rms)
                        continue
                    } else if (backgroundNoiseSamples.size == 5) {
                        adaptiveThreshold = max(voiceThreshold.toDouble(), backgroundNoiseSamples.average() * 2.5)
                        backgroundNoiseSamples.add(0.0)
                    }

                    rmsHistory.add(rms)
                    if (rmsHistory.size > 10) rmsHistory.removeAt(0)

                    val currentTime = System.currentTimeMillis()
                    if (rms > adaptiveThreshold) {
                        lastVoiceTime = currentTime
                        voiceDetected = true
                    }

                    // Feed PCM to FLAC encoder
                    feedFlacEncoder(processedChunk)

                    val recordingDuration = currentTime - recordingStartTime
                    if (voiceDetected &&
                        currentTime - lastVoiceTime > silenceThreshold &&
                        recordingDuration > minRecordingDuration) {

                        drainFlacEncoder(true) // finalize stream
                        withContext(Dispatchers.IO) {
                            val flac = encodedFlac.toByteArray()
                            if (flac.isNotEmpty()) {
                                onCapturingComplete(flac)
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

        val job = recordingJob
        recordingJob = null

        scope.launch(Dispatchers.IO) {
            job?.join()
            cleanupAudioResources()
        }
    }

    private fun cleanupAudioResources() {
        try {
            if (recorder.state == AudioRecord.STATE_INITIALIZED) recorder.stop()
            flacEncoder.stop()
            flacEncoder.release()
            encodedFlac.reset()
            recorder.release()
        } catch (e: Exception) {
            Log.e("AudioStreamer", "Cleanup failed", e)
        }
    }

    private fun setupFlacEncoder() {
        val format = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_FLAC, sampleRate, 1).apply {
            setInteger(MediaFormat.KEY_SAMPLE_RATE, sampleRate)
            setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1)
            setInteger(MediaFormat.KEY_PCM_ENCODING, AudioFormat.ENCODING_PCM_16BIT)
        }

        flacEncoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_FLAC)
        flacEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        flacEncoder.start()
    }

    private fun feedFlacEncoder(pcm: ByteArray) {
        val inputBufferIndex = flacEncoder.dequeueInputBuffer(10000)
        if (inputBufferIndex >= 0) {
            val inputBuffer = flacEncoder.getInputBuffer(inputBufferIndex)!!
            inputBuffer.clear()
            inputBuffer.put(pcm)
            flacEncoder.queueInputBuffer(inputBufferIndex, 0, pcm.size, System.nanoTime() / 1000, 0)
        }
        drainFlacEncoder(false)
    }

    private fun drainFlacEncoder(endOfStream: Boolean) {
        if (endOfStream) {
            val inputBufferIndex = flacEncoder.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                flacEncoder.queueInputBuffer(inputBufferIndex, 0, 0, System.nanoTime() / 1000, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            }
        }

        val bufferInfo = MediaCodec.BufferInfo()
        while (true) {
            val outputBufferIndex = flacEncoder.dequeueOutputBuffer(bufferInfo, 10000)
            if (outputBufferIndex >= 0) {
                val outputBuffer = flacEncoder.getOutputBuffer(outputBufferIndex)!!
                val data = ByteArray(bufferInfo.size)
                outputBuffer.get(data)
                encodedFlac.write(data)
                flacEncoder.releaseOutputBuffer(outputBufferIndex, false)
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) break
            } else {
                break
            }
        }
    }

    private fun calculateRMS(buffer: ByteArray): Double {
        var sum = 0.0
        val shortBuffer = ShortArray(buffer.size / 2)
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)
        for (sample in shortBuffer) sum += sample * sample
        return sqrt(sum / shortBuffer.size)
    }

    private fun applyGain(buffer: ByteArray, gain: Float): ByteArray {
        val shortBuffer = ShortArray(buffer.size / 2)
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)
        for (i in shortBuffer.indices) {
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