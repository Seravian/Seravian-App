package com.seravian.feat_chat.presentation.viewModel.chat

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message

data class ChatState(
    val currentChat: Chat ?= null,
    val messagesList: List<Message> = listOf(),
    val chatsList: List<Chat> = emptyList(),
    val createChatResult: NetworkResult<Chat, NetworkError> ?= null,
    val editChatResult: NetworkResult<Chat, NetworkError> ?= null,
    val deleteChatResult: EmptyResult<NetworkError> ?= null,
    val getChatsResult: NetworkResult<List<Chat>, NetworkError> ?= null,
    val getChatMessagesResult: NetworkResult<Pair<Chat, List<Message>>, NetworkError> ?= null,
    val joinChatResult: NetworkResult<Unit, NetworkError> ?= null
)
