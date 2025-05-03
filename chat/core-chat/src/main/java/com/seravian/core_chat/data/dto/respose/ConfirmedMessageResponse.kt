package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmedMessageResponse(
    val timestampUtc: String,
    val messageId: Long,
    val clientMessageId: String
)
