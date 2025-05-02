package com.seravian.core_chat.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatRequest(
    val title: String ?= null
)
