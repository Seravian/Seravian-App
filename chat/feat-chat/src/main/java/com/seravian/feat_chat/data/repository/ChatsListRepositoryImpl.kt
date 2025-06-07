package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import com.seravian.feat_chat.domain.repository.ChatsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class ChatsListRepositoryImpl(
    private val seravianChatBotDataSource: ChatBotRemoteDataSource,
    private val roomDataSource: LocalDataSource
): ChatsListRepository {
    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat, NetworkError> {
        val createChatResponse = seravianChatBotDataSource.createChat(createChatRequest)
        return createChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.insertChat(response.toEntity()) }
    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat, NetworkError> {
        val editChatResponse = seravianChatBotDataSource.updateChat(editChatRequest)
        return editChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.updateChat(response.toEntity()) }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        val deleteChatResponse = seravianChatBotDataSource.deleteChat(deleteChatRequest)
        return deleteChatResponse
            .onSuccess { roomDataSource.deleteChat(deleteChatRequest.id) }
    }

    override fun getChats(): Flow<NetworkResult<List<Chat>, NetworkError>> = channelFlow {
        val currentChats = mutableListOf<Chat>()

        // First emit from cache immediately
        roomDataSource.getChats()
            .onEach { cachedChats ->
                val extractedChats = cachedChats.map { it.extractChat() }
                currentChats.removeIf { currentChat ->
                    currentChat.id in extractedChats.map { it.id }
                }
                currentChats.addAll(extractedChats)
                send(NetworkResult.Success(extractedChats))
            }
            .launchIn(this)

        // Start fetching remote data
        seravianChatBotDataSource.getChats()
            .map { chats -> chats.map { it.extractChat() } }
            .onSuccess { remoteChats ->
                // Compare remote chats with local chats
                val currentChatIds = currentChats.map { it.id }
                val remoteChatIds = remoteChats.map { it.id }

                // Find new chats not in local storage
                val newChats = remoteChats.filter { chat ->
                    chat.id !in currentChatIds
                }

                // Find deleted chats that exist locally but not remotely
                val deletedChats = currentChats.filter { chat ->
                    chat.id !in remoteChatIds
                }

                // Only update if there are changes
                if (newChats.isNotEmpty()) {
                    newChats.forEach { roomDataSource.insertChat(it.toEntity()) }
                }

                if (deletedChats.isNotEmpty()) {
                    roomDataSource.deleteChats(deletedChats.map { it.toEntity() })
                    currentChats.removeAll(deletedChats.toSet())
                }
            }
            .onError { error ->
                send(NetworkResult.Error(error))
            }
    }.onCompletion {  }
}