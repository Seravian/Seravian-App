package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.ConnectionStatus
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.respose.AIResponse
import com.seravian.core_chat.data.dto.respose.ClientResponse
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ChatRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val roomDataSource: LocalDataSource
): ChatRepository {
    private val messagesList: MutableList<Message> = mutableListOf()
    private var currentChatId: String = ""

    //////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat, NetworkError> {
        val createChatResponse = remoteDataSource.createChat(createChatRequest)
        return createChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.insertChat(response.toEntity()) }
    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat, NetworkError> {
        val editChatResponse = remoteDataSource.updateChat(editChatRequest)
        return editChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.updateChat(response.toEntity()) }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        val deleteChatResponse = remoteDataSource.deleteChat(deleteChatRequest)
        return deleteChatResponse
            .onSuccess { roomDataSource.deleteChat(deleteChatRequest.id) }
    }

    override fun getChats(): Flow<NetworkResult<List<Chat>, NetworkError>> = channelFlow {
        roomDataSource.getChats()
            .onEach { cachedChats ->
                send(NetworkResult.Success(cachedChats.map { it.extractChat() }))
            }
            .launchIn(this)

        remoteDataSource.getChats()
            .map { chats -> chats.map { it.extractChat() } }
            .onSuccess { chats ->
                chats.forEach { roomDataSource.insertChat(it.toEntity()) }
            }
            .onError { error ->
                send(NetworkResult.Error(error))
            }
    }

    override fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>> = channelFlow {
        currentChatId = getChatMessagesRequest.id
        val storedChat = roomDataSource.getChat(getChatMessagesRequest.id)
        roomDataSource.getChatMessages(getChatMessagesRequest.id)
            .onEach { messages ->
                send(NetworkResult.Success(
                    storedChat.extractChat() to messages.map { it.extractMessage() }
                ))
            }
            .launchIn(this)

        messagesList.addAll(
            roomDataSource.getChatMessages(currentChatId)
                .first().map { it.extractMessage() }
        )

        if (messagesList.isEmpty()) {
            remoteDataSource.getChatMessages(getChatMessagesRequest)
                .map { response -> response.extractChat() to response.extractMessages() }
                .onSuccess { (_, messages) ->
                    roomDataSource.insertMessages(
                        messages.map { it.toEntity(currentChatId) }
                    )
                }
                .onError { error ->
                    send(NetworkResult.Error(error))
                }
        } else {
            syncMessages(
                SyncMessagesRequest(messagesList.last().timestamp, getChatMessagesRequest.id)
            )
        }
    }.onCompletion { messagesList.clear() }

    override suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): EmptyResult<NetworkError> {
        val syncMessagesResponse = remoteDataSource.syncMessages(syncRequest)
        return syncMessagesResponse.onSuccess { response ->
            roomDataSource.insertMessages(response.map { message ->
                message.extractMessage() }.map { it.toEntity(syncRequest.chatId) })
        }.map {  }
    }

    override suspend fun insertConfirmedMessage(message: Message) {
        roomDataSource.insertMessage(message.toEntity(currentChatId))
    }

    //////////////////////////////////
    ///////// REALTIME CHAT METHODS
    /////////////////////////////////

    override suspend fun startConnection() {
        remoteDataSource.startSignalRConnection()
    }

    override suspend fun stopConnection() {
        remoteDataSource.stopSignalRConnection()
    }

    override fun getSignalRConnectionStatus(): Flow<ConnectionStatus> {
        return remoteDataSource.getSignalRConnectionStatus()
    }

    override suspend fun joinChat(joinChatRequest: JoinChatRequest) {
        remoteDataSource.joinChat(joinChatRequest)
    }

    override suspend fun sendRequest(clientRequest: ClientRequest) {
        remoteDataSource.sendRequest(clientRequest)
    }

    override fun receiveClientResponse() {
        remoteDataSource.receiveClientResponse { response ->
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveAIResponse() {
        remoteDataSource.receiveAIResponse { response ->
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit) {
        remoteDataSource.receiveMessageConfirmation { callback(it) }
    }
}