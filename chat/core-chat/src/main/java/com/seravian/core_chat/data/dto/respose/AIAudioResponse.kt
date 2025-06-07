package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Audio
import kotlinx.serialization.Serializable

@Serializable
data class AIAudioResponse(
    val audioBytes: ByteArray,
    val contentType: String,
    val contentDisposition: String
) {
    fun extractAudio(audioId: Long): Audio {
        return Audio(
            audioId = audioId,
            audioBytes = audioBytes,
            contentType = contentType,
            fileName = extractFilename(contentDisposition) ?: "ai-response.wav"
        )
    }

    private fun extractFilename(contentDisposition: String): String? {
        val pattern = "filename=([^;\\s]+)".toRegex(RegexOption.IGNORE_CASE)
        return pattern.find(contentDisposition)?.groupValues?.get(1)
    }
}