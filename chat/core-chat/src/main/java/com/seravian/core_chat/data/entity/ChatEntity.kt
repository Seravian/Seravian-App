package com.seravian.core_chat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seravian.core_chat.domain.models.Chat

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",
    @ColumnInfo(name = "title")
    val title: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String = ""
) {
    fun extractChat() = Chat(id, title, createdAt)
}
