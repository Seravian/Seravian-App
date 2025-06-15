package com.seravian.core_chat.data.entity

import androidx.room.Dao
import androidx.room.Delete
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

    @Upsert
    suspend fun insertChat(chatEntity: ChatEntity)

    @Update
    suspend fun updateChat(chatEntity: ChatEntity)

    @Query("DELETE FROM chats WHERE id = :chatId")
    suspend fun deleteChat(chatId: String)

    @Delete
    suspend fun deleteChats(chats: List<ChatEntity>)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()

    @Query("SELECT * FROM messages WHERE chat_id = :chatId ORDER BY timestamp ASC")
    fun getChatMessages(chatId: String): Flow<List<MessageEntity>>

    @Upsert
    suspend fun insertMessage(message: MessageEntity)

    @Upsert
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Delete
    suspend fun deleteMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM diagnoses WHERE chat_id = :chatId ORDER BY requested_at_utc DESC")
    fun getChatDiagnoses(chatId: String): Flow<List<DiagnosisEntity>>

    @Query("SELECT * FROM diagnoses WHERE id = :diagnosisId")
    suspend fun getChatDiagnosis(diagnosisId: Long): DiagnosisEntity

    @Upsert
    suspend fun insertDiagnosis(diagnosis: DiagnosisEntity)

    @Upsert
    suspend fun insertDiagnoses(diagnoses: List<DiagnosisEntity>)

    @Query("DELETE FROM diagnoses WHERE id = :diagnosisId")
    suspend fun deleteDiagnosis(diagnosisId: Long)

    @Query("DELETE FROM diagnoses WHERE chat_id = :chatId")
    suspend fun deleteDiagnoses(chatId: String)
}