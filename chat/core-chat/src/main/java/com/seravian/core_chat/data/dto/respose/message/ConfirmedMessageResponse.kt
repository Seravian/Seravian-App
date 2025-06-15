package com.seravian.core_chat.data.dto.respose.message

import com.seravian.core_chat.domain.MessageType
import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmedMessageResponse(
    val messageId: Long,
    val chatId: String,
    val timestampUtc: String,
    val clientMessageId: String
) {
    fun buildMessage(messageContent: String): Message {
        return Message(
            id = Pair(messageId, null),
            content = messageContent,
            timestamp = timestampUtc,
            isAI = false,
            messageType = MessageType.TEXT
        )
    }
}
