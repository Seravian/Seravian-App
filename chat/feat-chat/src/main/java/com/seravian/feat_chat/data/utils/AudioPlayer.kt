package com.seravian.feat_chat.data.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import com.seravian.core_chat.domain.models.Audio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class AudioPlayer(
    private val onAmplitudeUpdate: (Float) -> Unit,
    private val onPlayBackStarted: (Long) -> Unit,
    private val onPlaybackComplete: () -> Unit,
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null
    private var isPlaying = false

    fun play(audio: Audio) {
        if (isPlaying) {
            stop()
        }

        try {
            val audioData = when (audio.contentType) {
                "audio/wav" -> audio.audioBytes
                else -> {
                    Log.e("AudioPlayer", "Unsupported audio format: ${audio.contentType}")
                    return
                }
            }

            startPlayback(audio.audioId, audioData)
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error preparing audio for playback", e)
            onPlaybackComplete()
        }
    }

    private fun startPlayback(audioId: Long, audioData: ByteArray) {
        // Initialize AudioTrack for 24kHz mono PCM
        val sampleRate = 24000
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
        scope.launch(Dispatchers.Main.immediate) {
            onPlayBackStarted(audioId)
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
                withContext(Dispatchers.Main.immediate) {
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