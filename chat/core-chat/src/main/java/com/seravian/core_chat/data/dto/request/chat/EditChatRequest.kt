package com.seravian.core_chat.data.dto.request.chat

import kotlinx.serialization.Serializable

@Serializable
data class EditChatRequest(
    val id: String,
    val title: String ?= null
)
