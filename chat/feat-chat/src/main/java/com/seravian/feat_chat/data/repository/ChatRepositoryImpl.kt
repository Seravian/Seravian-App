package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.data.ConnectionStatus
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatRemoteDataSource
import com.seravian.feat_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val chatDataSource: ChatRemoteDataSource,
    private val roomDataSource: LocalDataSource
): ChatRepository {
    private var currentChatId: String = ""

    //////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat, NetworkError> {
        val createChatResponse = chatDataSource.createChat(createChatRequest)
        return createChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.insertChat(response.toEntity()) }
    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat, NetworkError> {
        val editChatResponse = chatDataSource.updateChat(editChatRequest)
        return editChatResponse
            .map { response -> response.extractChat() }
            .onSuccess { response -> roomDataSource.updateChat(response.toEntity()) }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        val deleteChatResponse = chatDataSource.deleteChat(deleteChatRequest)
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
        chatDataSource.getChats()
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
    }.onCompletion { /* Clear any resources if needed */ }

    override fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>> = channelFlow {
        currentChatId = getChatMessagesRequest.id
        val storedChat = roomDataSource.getChat(getChatMessagesRequest.id)
        val messagesList: MutableList<Message> = mutableListOf()
        val cachedMessagesFlow = roomDataSource.getChatMessages(getChatMessagesRequest.id)

        // First emit from cache immediately
        cachedMessagesFlow
            .onEach { cachedMessages ->
                messagesList.removeIf { currentMessage ->
                    currentMessage.id in cachedMessages.map { it.extractMessage().id }
                }
                messagesList.addAll(cachedMessages.map { it.extractMessage() })
                send(NetworkResult.Success(
                    storedChat.extractChat() to cachedMessages.map { it.extractMessage() }
                ))
            }
            .launchIn(this)

        // Start fetching remote data
        chatDataSource.getChatMessages(getChatMessagesRequest)
            .map { response -> response.extractChat() to response.extractMessages() }
            .onSuccess { (_, remoteMessages) ->
                // Compare remote messages with local messages
                val currentMessageIds = messagesList.map { it.id }
                val remoteMessageIds = remoteMessages.map { it.id }

                // Find new messages not in local storage
                val newMessages = remoteMessages.filter { message ->
                    message.id !in currentMessageIds
                }

                // Find deleted messages that exist locally but not remotely
                val deletedMessages = messagesList.filter { message ->
                    message.id !in remoteMessageIds
                }

                // Only update if there are changes
                if (newMessages.isNotEmpty()) {
                    roomDataSource.insertMessages(
                        newMessages.map { it.toEntity(currentChatId) }
                    )
                }

                if (deletedMessages.isNotEmpty()) {
                    roomDataSource.deleteMessages(
                        deletedMessages.map { it.toEntity(currentChatId) }
                    )
                    messagesList.removeAll(deletedMessages.toSet())
                }
            }
            .onError { error ->
                send(NetworkResult.Error(error))
            }
    }.onCompletion {  }

    override suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): EmptyResult<NetworkError> {
        val syncMessagesResponse = chatDataSource.syncMessages(syncRequest)
        return syncMessagesResponse.onSuccess { response ->
            val remoteMessages = response.map { it.extractMessage() }
            val localMessages = roomDataSource.getChatMessages(syncRequest.chatId)
                .first()
                .map { it.extractMessage() }

            // Find messages that are in remote but not in local
            val newMessages = remoteMessages.filter { remoteMessage ->
                remoteMessage.id !in localMessages.map { it.id }
            }

            // Find messages that are in local but not in remote
            val deletedMessages = localMessages.filter { localMessage ->
                localMessage.id !in remoteMessages.map { it.id }
            }

            if (newMessages.isNotEmpty()) {
                roomDataSource.insertMessages(
                    newMessages.map { it.toEntity(syncRequest.chatId) }
                )
            }

            if (deletedMessages.isNotEmpty()) {
                roomDataSource.deleteMessages(
                    deletedMessages.map { it.toEntity(syncRequest.chatId) }
                )
            }
        }.map {  }
    }

    override suspend fun insertConfirmedMessage(message: Message) {
        roomDataSource.insertMessage(message.toEntity(currentChatId))
    }

    //////////////////////////////////
    ///////// REALTIME CHAT METHODS
    /////////////////////////////////

    override suspend fun startConnection() {
        chatDataSource.startSignalRConnection()
    }

    override suspend fun stopConnection() {
        chatDataSource.stopSignalRConnection()
    }

    override fun getSignalRConnectionStatus(): Flow<ConnectionStatus> {
        return chatDataSource.getSignalRConnectionStatus()
    }

    override suspend fun joinChat(joinChatRequest: JoinChatRequest) {
        chatDataSource.joinChat(joinChatRequest)
    }

    override suspend fun sendRequest(clientRequest: ClientRequest) {
        chatDataSource.sendRequest(clientRequest)
    }

    override fun receiveClientResponse() {
        chatDataSource.receiveClientResponse { response ->
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveAIResponse() {
        chatDataSource.receiveAIResponse { response ->
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit) {
        chatDataSource.receiveMessageConfirmation { callback(it) }
    }

    override suspend fun sendCapturedVoice(capturedVoice: ByteArray) {
        // TODO: Not yet implemented
    }
}