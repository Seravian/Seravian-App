package com.seravian.core_chat.data.dto.respose.chat

import kotlinx.serialization.Serializable

@Serializable
data class IsProcessingResponse(
    val isProcessing: Boolean
)
