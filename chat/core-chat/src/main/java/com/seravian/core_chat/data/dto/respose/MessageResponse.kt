package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: Long,
    val content: String,
    val timestampUtc: String,
    val isAI: Boolean
) {
    fun extractMessage() = Message(
        id = Pair(id, null),
        content = content,
        timestamp = timestampUtc,
        isAI = isAI
    )
}
