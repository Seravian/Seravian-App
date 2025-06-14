package com.seravian.core_chat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.seravian.core_chat.domain.MessageType
import com.seravian.core_chat.domain.models.Message

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chat_id"])]
)
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1,
    @ColumnInfo(name = "chat_id")
    val chatId: String = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: String = "",
    @ColumnInfo(name = "is_ai")
    val isAI: Boolean = false,
    @ColumnInfo(name = "message_type")
    val messageType: MessageType = MessageType.TEXT
) {
    fun extractMessage() = Message(
        id = Pair(id, null),
        content = content,
        timestamp = timestamp,
        isAI = isAI,
        messageType = messageType
    )
}
