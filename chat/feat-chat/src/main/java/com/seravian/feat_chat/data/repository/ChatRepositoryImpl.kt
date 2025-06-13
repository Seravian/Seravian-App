package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.seravian.core_chat.data.dto.request.message.ClientRequest
import com.seravian.core_chat.data.dto.request.chat.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCreationRequest
import com.seravian.core_chat.data.dto.request.message.SyncMessagesRequest
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCreationResponse
import com.seravian.core_chat.data.dto.respose.message.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.core_local.domain.LocalDataSource
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import com.seravian.feat_chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val seravianChatBotDataSource: ChatBotRemoteDataSource,
    private val roomDataSource: LocalDataSource,
    private val chatBotStateRepository: ChatBotStateRepository
): ChatRepository {
    private var currentChatId: String = ""

    //////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

    override fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>> = channelFlow {
        currentChatId = getChatMessagesRequest.id
        val messagesList = mutableListOf<Message>()
        val storedChat = roomDataSource.getChat(currentChatId)
        roomDataSource.getChatMessages(currentChatId)
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
            seravianChatBotDataSource.getChatMessages(getChatMessagesRequest)
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
                SyncMessagesRequest(messagesList.last().id.first ?: 0, currentChatId)
            )
        }
    }.onCompletion {  }

    private suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): EmptyResult<NetworkError> {
        val syncMessagesResponse = seravianChatBotDataSource.syncMessages(syncRequest)
        return syncMessagesResponse.onSuccess { response ->
            roomDataSource.insertMessages(response.map { message ->
                message.extractMessage() }.map { it.toEntity(syncRequest.chatId) })
        }.map {  }
    }

    override suspend fun insertConfirmedMessage(message: Message) {
        roomDataSource.insertMessage(message.toEntity(currentChatId))
    }

    override suspend fun sendDiagnosisCreationRequest(
        diagnosisCreationRequest: DiagnosisCreationRequest
    ): NetworkResult<DiagnosisCreationResponse, NetworkError> {
        return seravianChatBotDataSource.createDiagnosis(diagnosisCreationRequest)
    }

    //////////////////////////////////
    ///////// REALTIME CHAT METHODS
    /////////////////////////////////

    override suspend fun sendRequest(clientRequest: ClientRequest) {
        seravianChatBotDataSource.sendRequest(clientRequest)
    }

    override fun receiveClientResponse() {
        seravianChatBotDataSource.receiveClientResponse { response ->
            chatBotStateRepository.checkResponseProcessing()
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveAIResponse() {
        seravianChatBotDataSource.receiveAIResponse { response ->
            chatBotStateRepository.checkResponseProcessing()
            roomDataSource.insertMessage(response.extractMessage().toEntity(currentChatId))
        }
    }

    override fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit) {
        seravianChatBotDataSource.receiveMessageConfirmation { callback(it) }
    }
}