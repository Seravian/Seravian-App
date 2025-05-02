package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class EditChatResponse(
    val id:String,
    val title: String ?= null
)
