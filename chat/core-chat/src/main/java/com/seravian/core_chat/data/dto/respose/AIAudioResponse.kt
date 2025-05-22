package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class AIAudioResponse(
    val audioBytes: ByteArray,
    val contentType: String,
    val fileName: String
)
