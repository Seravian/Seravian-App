package com.seravian.feat_chat.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.domain.ConnectionStatus
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<Chat,NetworkError>

    suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<Chat,NetworkError>

    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError>

    suspend fun getChats(): NetworkResult<List<Chat>, NetworkError>

    suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<Pair<Chat, List<Message>>, NetworkError>

    suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): NetworkResult<List<Message>, NetworkError>

    suspend fun startConnection()

    suspend fun stopConnection()

    fun getSignalRConnectionStatus(): Flow<ConnectionStatus>

    suspend fun joinChat(joinChatRequest: JoinChatRequest)

    suspend fun sendRequest(clientRequest: ClientRequest)

    fun receiveClientResponse(callback: (Message) -> Unit)

    fun receiveAIResponse(callback: (Message) -> Unit)

    fun receiveMessageConfirmation(callback: (ConfirmedMessageResponse) -> Unit)
}