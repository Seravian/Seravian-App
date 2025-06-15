package com.seravian.feat_chat.presentation.viewModel.chat

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message

@Immutable
data class ChatState(
    val currentChat: Chat ?= null,
    val messagesList: List<Message> = listOf(),
    val isWaitingForResponse: Boolean = false,
    val isWaitingForDiagnosis: Boolean = false,
    val sendClientRequestResult: EmptyResult<NetworkError>? = null,
    val getChatMessagesResult: EmptyResult<NetworkError> ?= null,
    val joinChatResult: EmptyResult<NetworkError> ?= null,
    val diagnosisRequestResult: EmptyResult<NetworkError> ?= null
)
