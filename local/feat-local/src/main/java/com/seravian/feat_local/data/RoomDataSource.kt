package com.seravian.feat_local.data

import com.seravian.core_chat.data.entity.ChatDao
import com.seravian.core_chat.data.entity.ChatEntity
import com.seravian.core_chat.data.entity.MessageEntity
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.core_profile.data.local.ProfileDao
import com.seravian.core_profile.data.local.ProfileEntity
import kotlinx.coroutines.flow.Flow

class RoomDataSource(
    private val profileDao: ProfileDao,
    private val chatDao: ChatDao
): LocalDataSource {
    override suspend fun getProfile(): ProfileEntity {
        return profileDao.getProfile()
    }

    override suspend fun insertProfile(profileEntity: ProfileEntity) {
        profileDao.insertProfile(profileEntity)
    }

    override suspend fun updateProfile(profileEntity: ProfileEntity) {
        profileDao.updateProfile(profileEntity)
    }

    override suspend fun deleteProfile() {
        profileDao.deleteProfile()
    }

    override suspend fun getChat(chatId: String): ChatEntity {
        return chatDao.getChat(chatId)
    }

    override suspend fun getChats(): Flow<List<ChatEntity>> {
        return chatDao.getChats()
    }

    override suspend fun insertChat(chatEntity: ChatEntity) {
        chatDao.insertChat(chatEntity)
    }

    override suspend fun updateChat(chatEntity: ChatEntity) {
        chatDao.updateChat(chatEntity)
    }

    override suspend fun deleteChat(chatId: String) {
        chatDao.deleteChat(chatId)
    }

    override suspend fun deleteChats() {
        chatDao.deleteChats()
    }

    override suspend fun getChatMessages(chatId: String): Flow<List<MessageEntity>> {
        return chatDao.getChatMessages(chatId)
    }

    override suspend fun insertMessage(message: MessageEntity) {
        chatDao.insertMessage(message)
    }

    override suspend fun insertMessages(messages: List<MessageEntity>) {
        chatDao.insertMessages(messages)
    }
}