package com.seravian.core_chat.data.dto.request.message

import kotlinx.serialization.Serializable

@Serializable
data class SendClientRequest(
    val chatId: String,
    val messageClientId: String,
    val message: String
)
