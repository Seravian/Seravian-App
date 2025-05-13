package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Chat
import kotlinx.serialization.Serializable

@Serializable
data class CreateChatResponse(
    val id: String,
    val title: String ?= null,
    val createdAtUtc: String
) {
    fun extractChat() = Chat(id, title, createdAtUtc)
}
