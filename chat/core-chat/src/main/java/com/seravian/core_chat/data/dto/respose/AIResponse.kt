package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class AIResponse(
    val id: Long,
    val message: String,
    val timestampUtc: String
) {
    fun extractMessage() = Message(
        id = Pair(id, null),
        content = message,
        timestamp = timestampUtc,
        isAI = true
    )
}
