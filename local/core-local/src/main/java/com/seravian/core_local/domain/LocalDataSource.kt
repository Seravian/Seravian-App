package com.seravian.core_local.domain

import com.seravian.core_chat.data.entity.ChatEntity
import com.seravian.core_chat.data.entity.DiagnosisEntity
import com.seravian.core_chat.data.entity.MessageEntity
import com.seravian.core_profile.data.local.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getProfile(): ProfileEntity
    suspend fun insertProfile(profileEntity: ProfileEntity)
    suspend fun updateProfile(profileEntity: ProfileEntity)
    suspend fun deleteProfile()
    suspend fun getChat(chatId: String): ChatEntity
    fun getChats(): Flow<List<ChatEntity>>
    suspend fun insertChat(chatEntity: ChatEntity)
    suspend fun updateChat(chatEntity: ChatEntity)
    suspend fun deleteChat(chatId: String)
    suspend fun deleteChats(chats: List<ChatEntity>)
    suspend fun deleteAllChats()
    fun getChatMessages(chatId: String): Flow<List<MessageEntity>>
    suspend fun insertMessage(message: MessageEntity)
    suspend fun insertMessages(messages: List<MessageEntity>)
    suspend fun deleteMessages(messages: List<MessageEntity>)
    fun getChatDiagnoses(chatId: String): Flow<List<DiagnosisEntity>>
    suspend fun insertDiagnosis(diagnosis: DiagnosisEntity)
    suspend fun insertDiagnoses(diagnoses: List<DiagnosisEntity>)
    suspend fun deleteAllData()
    suspend fun deleteDiagnosis(diagnosisId: Long)
    suspend fun deleteDiagnoses(chatId: String)
}