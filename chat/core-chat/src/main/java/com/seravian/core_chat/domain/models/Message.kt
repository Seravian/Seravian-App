package com.seravian.core_chat.domain.models

import com.seravian.core_chat.data.entity.MessageEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Message(
    val id: Pair<Long?, String?> = Pair(null, null),
    val content: String = "",
    val timestamp: String = "",
    val isAI: Boolean = false
) {
    fun formatDateTime(): String {
        val cleanedTimestamp = if (timestamp.contains(".")) {
            timestamp.substringBefore(".") + "Z"
        } else {
            if (!timestamp.endsWith("Z")) timestamp + "Z" else timestamp
        }

        val instant = Instant.parse(cleanedTimestamp)
        return DateTimeFormatter
            .ofPattern("d MMM, yyyy  h:mm a")  // 2 May, 2025  2:30 PM
            .withZone(ZoneId.systemDefault())
            .format(instant)
    }

    fun toEntity(chatId: String) = MessageEntity(
        id = id.first ?: 0,
        chatId = chatId,
        content = content,
        timestamp = timestamp,
        isAI = isAI
    )
}
