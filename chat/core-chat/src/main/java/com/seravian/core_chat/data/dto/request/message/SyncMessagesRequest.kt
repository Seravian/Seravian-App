package com.seravian.core_chat.data.dto.request.message

import kotlinx.serialization.Serializable

@Serializable
data class SyncMessagesRequest(
    val lastMessageId: Long,
    val chatId: String
)
