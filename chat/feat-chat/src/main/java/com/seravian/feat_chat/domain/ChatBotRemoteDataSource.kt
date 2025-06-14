package com.seravian.feat_chat.domain

import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.message.ClientRequest
import com.seravian.core_chat.data.dto.request.chat.CreateChatRequest
import com.seravian.core_chat.data.dto.request.chat.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.chat.EditChatRequest
import com.seravian.core_chat.data.dto.request.voice.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.chat.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.chat.IsProcessingRequest
import com.seravian.core_chat.data.dto.request.chat.JoinChatRequest
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosesDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCheckRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCreationRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDetailsRequest
import com.seravian.core_chat.data.dto.request.message.SyncMessagesRequest
import com.seravian.core_chat.data.dto.request.voice.UploadVoiceRequest
import com.seravian.core_chat.data.dto.respose.voice.AIAudioReadyResponse
import com.seravian.core_chat.data.dto.respose.voice.AIAudioResponse
import com.seravian.core_chat.data.dto.respose.message.AIResponse
import com.seravian.core_chat.data.dto.respose.message.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.chat.ChatResponse
import com.seravian.core_chat.data.dto.respose.message.ClientResponse
import com.seravian.core_chat.data.dto.respose.message.ConfirmedMessageResponse
import com.seravian.core_chat.data.dto.respose.chat.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.chat.EditChatResponse
import com.seravian.core_chat.data.dto.respose.chat.IsProcessingResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.ChatDiagnosisResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCheckResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCreationResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisDetailsResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisReadyResponse
import com.seravian.core_chat.data.dto.respose.message.MessageResponse
import kotlinx.coroutines.flow.Flow

interface ChatBotRemoteDataSource {
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

    suspend fun isProcessing(
        isProcessingRequest: IsProcessingRequest
    ): NetworkResult<IsProcessingResponse, NetworkError>

    suspend fun fetchAIAudioResponse(
        fetchAIAudioRequest: FetchAIAudioRequest
    ): NetworkResult<AIAudioResponse, NetworkError>

    suspend fun createDiagnosis(
        creationRequest: DiagnosisCreationRequest
    ): NetworkResult<DiagnosisCreationResponse, NetworkError>

    suspend fun getChatDiagnoses(
        chatDiagnosesRequest: ChatDiagnosesRequest
    ): NetworkResult<List<ChatDiagnosisResponse>, NetworkError>

    suspend fun getDiagnosisDetails(
        chatDiagnosesRequest: DiagnosisDetailsRequest
    ): NetworkResult<DiagnosisDetailsResponse, NetworkError>

    suspend fun isDiagnosing(
        diagnosingCheckRequest: DiagnosisCheckRequest
    ): NetworkResult<DiagnosisCheckResponse, NetworkError>

    suspend fun deleteDiagnosis(
        diagnosisDeletionRequest: DiagnosisDeletionRequest
    ): EmptyResult<NetworkError>

    suspend fun deleteDiagnoses(
        diagnosesDeletionRequest: DiagnosesDeletionRequest
    ): EmptyResult<NetworkError>

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

    fun receiveDiagnosisReadyResponse(
        callback: suspend (DiagnosisReadyResponse) -> Unit
    )
}