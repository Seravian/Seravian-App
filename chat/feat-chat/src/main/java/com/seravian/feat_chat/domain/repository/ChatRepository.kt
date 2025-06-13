package com.seravian.feat_chat.domain.repository

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.message.ClientRequest
import com.seravian.core_chat.data.dto.request.chat.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCreationRequest
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCreationResponse
import com.seravian.core_chat.data.dto.respose.message.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>>

    suspend fun insertConfirmedMessage(message: Message)

    suspend fun sendDiagnosisCreationRequest(
        diagnosisCreationRequest: DiagnosisCreationRequest
    ): NetworkResult<DiagnosisCreationResponse, NetworkError>

    suspend fun sendRequest(clientRequest: ClientRequest)

    fun receiveClientResponse()

    fun receiveAIResponse()

    fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit)
}