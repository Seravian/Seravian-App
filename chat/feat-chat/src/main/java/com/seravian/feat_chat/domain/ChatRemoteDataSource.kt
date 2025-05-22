package com.seravian.feat_chat.domain

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.ConnectionStatus
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.request.UploadVoiceRequest
import com.seravian.core_chat.data.dto.respose.AIAudioReadyResponse
import com.seravian.core_chat.data.dto.respose.AIAudioResponse
import com.seravian.core_chat.data.dto.respose.AIResponse
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.ClientResponse
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_chat.data.dto.respose.MessageResponse
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {
    //////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

    suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<CreateChatResponse, NetworkError>

    suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<EditChatResponse, NetworkError>

    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest):EmptyResult<NetworkError>

    suspend fun getChats(): NetworkResult<List<ChatResponse>, NetworkError>

    suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<ChatMessagesResponse, NetworkError>

    suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): NetworkResult<List<MessageResponse>, NetworkError>

    suspend fun uploadUserVoice(
        uploadVoiceRequest: UploadVoiceRequest
    ): EmptyResult<NetworkError>

    suspend fun fetchAIAudioResponse(
        fetchAIAudioRequest: FetchAIAudioRequest
    ): NetworkResult<AIAudioResponse, NetworkError>

    //////////////////////////////////
    ///////// REALTIME CHAT METHODS
    /////////////////////////////////

    suspend fun startSignalRConnection()

    suspend fun stopSignalRConnection()

    fun getSignalRConnectionStatus(): Flow<ConnectionStatus>

    suspend fun joinChat(joinChatRequest: JoinChatRequest)

    suspend fun sendRequest(clientRequest: ClientRequest)

    fun receiveClientResponse(callback: suspend (ClientResponse) -> Unit)

    fun receiveAIResponse(callback: suspend (AIResponse) -> Unit)

    fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit)

    suspend fun receiveAIAudioReadyResponse(
        callback: suspend (AIAudioReadyResponse) -> Unit
    )
}