package com.seravian.core_chat.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChat(chatId: String): ChatEntity

    @Query("SELECT * FROM chats")
    suspend fun getChats(): List<ChatEntity>

    @Insert
    suspend fun insertChat(chatEntity: ChatEntity)

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("SELECT * FROM messages WHERE chatId = :chatId")
    suspend fun getChatMessages(chatId: String): List<MessageEntity>

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Insert
    suspend fun insertMessages(messages: List<MessageEntity>)
}