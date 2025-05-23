package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.ErrorType
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.request.UploadVoiceRequest
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Audio
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatRemoteDataSource
import com.seravian.feat_chat.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex

class ChatRepositoryImpl(
    private val chatDataSource: ChatRemoteDataSource,
    private val roomDataSource: LocalDataSource
): ChatRepository {
    private val audioDownloadLock: Mutex = Mutex()
    private var downloadExecution: Deferred<NetworkResult<Audio, NetworkError>>? = null

    private var currentChatId: String = ""
    private var lastNotifiedAudio: Long = -1

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
    }.onCompletion {  }

    override fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>> = channelFlow {
        currentChatId = getChatMessagesRequest.id
        val messagesList = mutableListOf<Message>()
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
            chatDataSource.getChatMessages(getChatMessagesRequest)
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
                SyncMessagesRequest(messagesList.last().id.first ?: 0, getChatMessagesRequest.id)
            )
        }
    }.onCompletion {  }

    private suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): EmptyResult<NetworkError> {
        val syncMessagesResponse = chatDataSource.syncMessages(syncRequest)
        return syncMessagesResponse.onSuccess { response ->
            roomDataSource.insertMessages(response.map { message ->
                message.extractMessage() }.map { it.toEntity(syncRequest.chatId) })
        }.map {  }
    }

    override suspend fun insertConfirmedMessage(message: Message) {
        roomDataSource.insertMessage(message.toEntity(currentChatId))
    }

    override suspend fun sendCapturedVoice(capturedVoice: ByteArray): EmptyResult<NetworkError> {
        return chatDataSource.uploadUserVoice(UploadVoiceRequest(capturedVoice, currentChatId))
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

    override suspend fun receiveAIAudioResponse(callback: (NetworkResult<Audio, NetworkError>) -> Unit) {
        chatDataSource.receiveAIAudioReadyResponse { aiAudioReadyResponse ->
            lastNotifiedAudio = aiAudioReadyResponse.aiAudioId
            val fetchingResult = downloadAudio(
                aiAudioReadyResponse.extractFetchRequest()
            )
            callback(fetchingResult)
        }
    }

    override suspend fun fetchAIAudio(
        fetchRequest: FetchAIAudioRequest
    ): NetworkResult<Audio, NetworkError> {
        when {
            fetchRequest.aiAudioId == lastNotifiedAudio && downloadExecution != null -> {
                return NetworkResult.Error(NetworkError(ErrorType.TOO_MANY_REQUESTS))
            }
            fetchRequest.aiAudioId != lastNotifiedAudio && downloadExecution == null -> {
                lastNotifiedAudio = fetchRequest.aiAudioId
                return downloadAudio(fetchRequest)
            }
            fetchRequest.aiAudioId == lastNotifiedAudio && downloadExecution == null -> {
                lastNotifiedAudio = fetchRequest.aiAudioId
                return downloadAudio(fetchRequest)
            }
            else -> { return NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR)) }
        }
    }

    private suspend fun downloadAudio(
        fetchRequest: FetchAIAudioRequest
    ): NetworkResult<Audio, NetworkError> {
        if (!audioDownloadLock.tryLock()) {
            downloadExecution?.let {
                println("Already Fetching Audio")
                return NetworkResult.Error(NetworkError(ErrorType.TOO_MANY_REQUESTS))
            }
        }

        return try {
            val deferred = CoroutineScope(Dispatchers.IO).async {
                chatDataSource.fetchAIAudioResponse(fetchRequest).map { response ->
                    response.extractAudio(fetchRequest.aiAudioId)
                }
            }
            downloadExecution = deferred

            deferred.await()
        } finally {
            audioDownloadLock.unlock()
            downloadExecution = null
        }
    }
}