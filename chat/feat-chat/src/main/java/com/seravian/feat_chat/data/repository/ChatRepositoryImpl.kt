package com.seravian.feat_chat.data.repository

import android.util.Log
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
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
import com.seravian.feat_chat.domain.ChatRepository

class ChatRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): ChatRepository {
    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<CreateChatResponse, NetworkError> {
        val createChatResponse = remoteDataSource.createChat(createChatRequest)
        return createChatResponse
            .onSuccess { response ->
                // Optional: log or update local data
                Log.d("Chat", "Chat created with id = ${response.id}, title = ${response.title}")
            }

    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<EditChatResponse, NetworkError> {
        val editChatResponse = remoteDataSource.updateChat(editChatRequest)
        return editChatResponse
            .onSuccess { response ->
                Log.d("Chat", "Chat created with id = ${response.id}, new title = ${response.title}")
            }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        val deleteChatResponse = remoteDataSource.deleteChat(deleteChatRequest)
        return deleteChatResponse
            .onSuccess { response ->
                Log.d("Chat", "Chat has been deleted successfully")
            }
    }

    override suspend fun getChats(): NetworkResult<List<ChatResponse>, NetworkError> {
        val chatsResponse = remoteDataSource.getChats()
        return chatsResponse
            .onSuccess { response ->
                Log.d("Chat", "${response.size} Chats fetched successfully")
            }
    }

    override suspend fun getChatMessages(getChatMessagesRequest: GetChatMessagesRequest): NetworkResult<ChatMessagesResponse, NetworkError> {
        val chatMessagesResponse = remoteDataSource.getChatMessages(getChatMessagesRequest)
        return chatMessagesResponse
            .onSuccess { response ->
                Log.d("Chat", "${response.messages.size} Messages fetched successfully from ${response.title}")
            }
    }
}