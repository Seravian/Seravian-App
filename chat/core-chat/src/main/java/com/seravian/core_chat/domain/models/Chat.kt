package com.seravian.core_chat.domain.models

import com.seravian.core_chat.data.entity.ChatEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Chat(
    val id: String = "",
    val title: String? = null,
    val createdAt: String = "",
) {
    fun formatDateTime(): String {
        val cleanedCreatedAt = if (createdAt.contains(".")) {
            createdAt.substringBefore(".") + "Z"
        } else {
            if (!createdAt.endsWith("Z")) createdAt + "Z" else createdAt
        }

        val instant = Instant.parse(cleanedCreatedAt)
        return DateTimeFormatter
            .ofPattern("d MMM, yyyy")  // "5 Jun, 2023"
            .withZone(ZoneId.systemDefault())
            .format(instant)
    }

    fun toEntity() = ChatEntity(id, title, createdAt)
}
