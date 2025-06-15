package com.seravian.core_chat.data.dto.request.voice

import kotlinx.serialization.Serializable

@Serializable
data class UploadVoiceRequest(
    val voiceBytes: ByteArray,
    val chatId: String
)