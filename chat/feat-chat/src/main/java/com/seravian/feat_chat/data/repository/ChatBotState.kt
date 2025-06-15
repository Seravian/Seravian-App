package com.seravian.feat_chat.data.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Diagnosis
import com.seravian.core_chat.domain.models.Message

data class ChatBotState(
    val currentChat: Chat ?= null,
    val lastMessage: Message ?= null,
    val lastNotifiedDiagnosisId: Long ?= null,
    val isWaitingForResponse: Boolean = false,
    val isWaitingForDiagnosis: Boolean = false,
    val joinChatResult: EmptyResult<NetworkError> ?= null
)
