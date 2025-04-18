package com.seravian.feat_chat.presentation.viewModel

import com.seravian.core_chat.domain.models.Message

data class ChatState(
    val messagesList: List<Message> = emptyList(),
)
