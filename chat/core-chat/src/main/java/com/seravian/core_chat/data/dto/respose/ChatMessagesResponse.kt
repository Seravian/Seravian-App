package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessagesResponse(
    val id: String,
    val title: String,
    val createdAt: String,
    val messages: List<MessageResponse>
) {
    fun extractChat(): Chat {
        return Chat(
            id = id,
            title = title,
            createdAt = createdAt
        )
    }

    fun extractMessages(): List<Message> {
        return messages.map { it.extractMessage() }
    }
}
