package com.seravian.feat_chat.presentation.viewModel.chats_list

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_chat.domain.models.Chat

@Immutable
data class ChatsListState(
    val chatsList: List<Chat> = emptyList(),
    val createChatResult: NetworkResult<Chat, NetworkError> ?= null,
    val editChatResult: NetworkResult<Chat, NetworkError> ?= null,
    val deleteChatResult: EmptyResult<NetworkError> ?= null,
    val getChatsResult: NetworkResult<List<Chat>, NetworkError> ?= null,
)
