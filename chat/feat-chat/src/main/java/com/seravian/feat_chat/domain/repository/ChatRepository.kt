package com.seravian.feat_chat.domain.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.chat.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCreationRequest
import com.seravian.core_chat.data.dto.request.message.SendClientRequest
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCreationResponse
import com.seravian.core_chat.data.dto.respose.message.ClientResponse
import com.seravian.core_chat.data.dto.respose.message.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendClientRequest(
        clientRequest: SendClientRequest
    ): EmptyResult<NetworkError>

    fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>>

    suspend fun insertConfirmedMessage(message: Message)

    suspend fun sendDiagnosisCreationRequest(
        diagnosisCreationRequest: DiagnosisCreationRequest
    ): NetworkResult<DiagnosisCreationResponse, NetworkError>

    fun receiveClientResponse()

    fun receiveAIResponse()
}