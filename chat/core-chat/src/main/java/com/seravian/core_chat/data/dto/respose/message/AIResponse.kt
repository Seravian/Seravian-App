package com.seravian.core_chat.data.dto.respose.message

import com.seravian.core_chat.domain.MessageType
import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class AIResponse(
    val id: Long,
    val chatId: String,
    val message: String,
    val timestampUtc: String,
    val messageType: Int
) {
    fun extractMessage() = Message(
        id = Pair(id, null),
        content = message,
        timestamp = timestampUtc,
        isAI = true,
        messageType = MessageType.entries[messageType]
    )
}
