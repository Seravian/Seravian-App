package com.seravian.core_chat.domain.models

data class Audio(
    val audioId: Long,
    val audioBytes: ByteArray,
    val contentType: String,
    val fileName: String
)
