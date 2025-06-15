package com.seravian.feat_chat.domain.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.chat.CreateChatRequest
import com.seravian.core_chat.data.dto.request.chat.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.chat.EditChatRequest
import com.seravian.core_chat.domain.models.Chat
import kotlinx.coroutines.flow.Flow

interface ChatsListRepository {
    suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat, NetworkError>

    suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat, NetworkError>

    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError>

    fun getChats(): Flow<NetworkResult<List<Chat>, NetworkError>>
}