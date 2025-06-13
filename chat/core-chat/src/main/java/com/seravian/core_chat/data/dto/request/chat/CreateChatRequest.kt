package com.seravian.core_chat.data.dto.request.chat

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    val title: String ?= "New Chat"
)
