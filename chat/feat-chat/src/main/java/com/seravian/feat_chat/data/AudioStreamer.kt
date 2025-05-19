package com.seravian.feat_chat.data

import android.Manifest
import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.NoiseSuppressor
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class AudioStreamer(
    private val scope: CoroutineScope,
    private val onChunkReady: (ByteArray) -> Unit,
    private val onUserStoppedTalking: () -> Unit,
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

    private var isRecording = false
    private var recordingJob: Job ?= null

    @SuppressLint("MissingPermission")
    fun start() {
        if (isRecording) return
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        ).also {
            if (NoiseSuppressor.isAvailable()) {
                NoiseSuppressor.create(it.audioSessionId)
            }
        }
        isRecording = true
        recorder.startRecording()

        recordingJob = scope.launch(Dispatchers.IO) {
            val buffer = ByteArray(bufferSize)
            var lastVoiceTime = System.currentTimeMillis()

            while (isRecording && recorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                val read = recorder.read(buffer, 0, buffer.size)
                if (read > 0) {
                    val chunk = buffer.copyOf(read)
                    onChunkReady(chunk)

                    val rms = calculateRMS(chunk)
                    onAmplitudeUpdate(rms.toFloat())

                    val currentTime = System.currentTimeMillis()

                    if (rms > 500) { // Threshold to detect voice
                        lastVoiceTime = currentTime
                    }

                    if (currentTime - lastVoiceTime > silenceThreshold) {
                        withContext(Dispatchers.Main) {
                            onUserStoppedTalking()
                        }
                        stop()
                    }
                }
            }
        }
    }

    fun stop() {
        isRecording = false
        recorder.stop()
        recorder.release()
        recordingJob?.cancel()
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
