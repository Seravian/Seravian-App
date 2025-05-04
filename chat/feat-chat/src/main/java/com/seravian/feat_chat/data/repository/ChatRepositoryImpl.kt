package com.seravian.feat_chat.data.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
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
import com.seravian.feat_chat.domain.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ChatRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): ChatRepository {

    /////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

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

    override suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): NetworkResult<List<Message>, NetworkError> {
        val syncMessagesResponse = remoteDataSource.syncMessages(syncRequest)
        return syncMessagesResponse.map { response -> response.extractMessages() }
    }

    /////////////////////////////////
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

    override fun receiveClientResponse(callback: (Message) -> Unit) {
       remoteDataSource.receiveClientResponse { response ->
           callback(response.extractMessage())
       }
    }

    override fun receiveAIResponse(callback: (Message) -> Unit) {
        remoteDataSource.receiveAIResponse { response ->
            callback(response.extractMessage())
        }
    }

    override fun receiveMessageConfirmation(callback: (ConfirmedMessageResponse) -> Unit) {
        remoteDataSource.receiveMessageConfirmation { confirmation ->
            callback(confirmation)
        }
    }
}