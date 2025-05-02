package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Chat
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: String,
    val title: String,
    val createdAt: String
) {
    fun extractChat() = Chat(id, title, createdAt)
}
