package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmedMessageResponse(
    val messageId: Long,
    val chatId: String,
    val timestampUtc: String,
    val clientMessageId: String
)
