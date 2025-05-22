package com.seravian.feat_chat.data

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import com.seravian.core_chat.data.dto.respose.AIAudioResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class AudioPlayer(
    private val scope: CoroutineScope,
    private val onAmplitudeUpdate: (Float) -> Unit,
    private val onPlayBackStarted: suspend () -> Unit,
    private val onPlaybackComplete: () -> Unit,
) {
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private var isPlaying = false

    fun play(audioResponse: AIAudioResponse) {
        if (isPlaying) {
            stop()
        }

        scope.launch(Dispatchers.IO) {
            try {
                val audioData = when (audioResponse.contentType) {
                    "audio/wav" -> audioResponse.audioBytes
                    else -> {
                        Log.e("AudioPlayer", "Unsupported audio format: ${audioResponse.contentType}")
                        return@launch
                    }
                }

                startPlayback(audioData)
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Error preparing audio for playback", e)
                onPlaybackComplete()
            }
        }
    }

    private fun startPlayback(audioData: ByteArray) {
        // Initialize AudioTrack for 16kHz mono PCM
        val sampleRate = 16000
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            channelConfig,
            audioFormat
        )

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build(),
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(audioFormat)
                .setChannelMask(channelConfig)
                .build(),
            bufferSize,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )

        audioTrack?.play()
        isPlaying = true
        scope.launch(Dispatchers.Main) {
            onPlayBackStarted()
        }

        // Stream the audio data and calculate amplitude
        playbackJob = scope.launch(Dispatchers.IO) {
            val chunkSize = 4096 // Process in chunks for amplitude calculation
            var offset = 0

            while (isPlaying && offset < audioData.size) {
                val bytesToWrite = minOf(chunkSize, audioData.size - offset)
                val chunk = audioData.copyOfRange(offset, offset + bytesToWrite)

                // Calculate amplitude from this chunk and update
                val rms = calculateRMS(chunk)
                withContext(Dispatchers.Main) {
                    onAmplitudeUpdate(rms.toFloat())
                }

                // Write data to AudioTrack
                audioTrack?.write(chunk, 0, bytesToWrite)
                offset += bytesToWrite
            }

            if (isPlaying) {
                withContext(Dispatchers.Main) {
                    onPlaybackComplete()
                }
                stop()
            }
        }
    }

    fun stop() {
        audioTrack?.apply {
            stop()
            release()
        }
        audioTrack = null

        playbackJob?.cancel()
        playbackJob = null

        if (!isPlaying) return
        isPlaying = false
    }

    private fun calculateRMS(buffer: ByteArray): Double {
        var sum = 0.0
        val shortBuffer = ShortArray(buffer.size / 2)
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortBuffer)
        for (sample in shortBuffer) sum += sample * sample
        return sqrt(sum / shortBuffer.size)
    }
}