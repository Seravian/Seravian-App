package com.seravian.core_chat.domain.models

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Message(
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
            .ofPattern("dd/MM/yyyy h:mm a")  // 05/06/2023 2:30 PM
            .withZone(ZoneId.systemDefault())
            .format(instant)
    }
}
