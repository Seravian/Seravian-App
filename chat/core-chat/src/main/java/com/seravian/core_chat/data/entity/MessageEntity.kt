package com.seravian.core_chat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.seravian.core_chat.domain.models.Message

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1,
    @ColumnInfo(name = "chatId")
    val chatId: String = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: String = "",
    @ColumnInfo(name = "isAI")
    val isAI: Boolean = false
) {
    fun extractMessage() = Message(
        id = Pair(id, null),
        content = content,
        timestamp = timestamp,
        isAI = isAI
    )
}
