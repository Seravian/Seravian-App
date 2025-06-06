package com.seravian.feat_chat.domain.repository

import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.domain.models.Audio
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): Flow<NetworkResult<Pair<Chat, List<Message>>, NetworkError>>

    suspend fun insertConfirmedMessage(message: Message)

    suspend fun sendRequest(clientRequest: ClientRequest)

    fun receiveClientResponse()

    fun receiveAIResponse()

    fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit)
}