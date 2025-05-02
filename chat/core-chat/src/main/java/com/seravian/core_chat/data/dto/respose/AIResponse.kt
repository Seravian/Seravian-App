package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class AIResponse(
    val message: String,
    val timestampUtc: String
)
