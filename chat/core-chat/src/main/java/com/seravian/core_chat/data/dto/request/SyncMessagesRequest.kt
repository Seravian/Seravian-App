package com.seravian.core_chat.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SyncMessagesRequest(
    val lastMessageTimestampUtc: String,
    val chatId: String
)
