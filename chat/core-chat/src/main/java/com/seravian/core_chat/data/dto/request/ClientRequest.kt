package com.seravian.core_chat.data.dto.request

import com.seravian.core_chat.domain.models.Message
import kotlinx.serialization.Serializable
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ClientRequest(
    val messageClientId: String,
    val message: String
) {
    fun buildMessage(): Message {
        return Message(
            id = Pair(null, messageClientId),
            content = message,
            timestamp = ZonedDateTime
                .now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT),
            isAI = false
        )
    }
}
