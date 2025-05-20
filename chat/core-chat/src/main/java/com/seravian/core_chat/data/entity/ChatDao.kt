package com.seravian.core_chat.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChat(chatId: String): ChatEntity

    @Query("SELECT * FROM chats ORDER BY created_at DESC")
    fun getChats(): Flow<List<ChatEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chatEntity: ChatEntity)

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteChats()

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getChatMessages(chatId: String): Flow<List<MessageEntity>>

    @Upsert
    suspend fun insertMessage(message: MessageEntity)

    @Insert
    suspend fun insertMessages(messages: List<MessageEntity>)
}