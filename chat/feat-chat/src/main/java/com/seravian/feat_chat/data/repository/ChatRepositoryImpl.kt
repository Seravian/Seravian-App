package com.seravian.feat_chat.data.repository

import android.util.Log
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.domain.ChatRepository

class ChatRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): ChatRepository {
    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat, NetworkError> {
        val createChatResponse = remoteDataSource.createChat(createChatRequest)
        return createChatResponse.map { response -> response.extractChat() }
    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat, NetworkError> {
        val editChatResponse = remoteDataSource.updateChat(editChatRequest)
        return editChatResponse.map { response -> response.extractChat() }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        val deleteChatResponse = remoteDataSource.deleteChat(deleteChatRequest)
        return deleteChatResponse
    }

    override suspend fun getChats(): NetworkResult<List<Chat>, NetworkError> {
        val chatsResponse = remoteDataSource.getChats()
        return chatsResponse.map { chats -> chats.map { it.extractChat() } }
    }

    override suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<Pair<Chat, List<Message>>, NetworkError> {
        val chatMessagesResponse = remoteDataSource.getChatMessages(getChatMessagesRequest)
        return chatMessagesResponse.map { response -> response.extractChat() to response.extractMessages() }
    }
}