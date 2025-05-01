package com.seravian.feat_chat.presentation.viewModel

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message

data class ChatState(
    val currentChat: Chat ?= null,
    val messagesList: MutableList<Message> ?= null,
    val chatsList: List<Chat> ?= null,
    val createChatResult: NetworkResult<CreateChatResponse, NetworkError> ?= null,
    val editChatResult: NetworkResult<EditChatResponse, NetworkError> ?= null,
    val deleteChatResult: EmptyResult<NetworkError> ?= null,
    val getChatsResult: NetworkResult<List<ChatResponse>, NetworkError> ?= null,
    val getChatMessagesResult: NetworkResult<ChatMessagesResponse, NetworkError> ?= null
)
