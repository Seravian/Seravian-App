package com.seravian.feat_chat.presentation.models

import com.seravian.core_chat.domain.models.Message

data class MessageUI(
    val id: Int = 0,
    val content: String = "",
    val timestamp: String = "",
    val isAI: Boolean = false
)

fun Message.toMessageUI(id: Int) = MessageUI(
    id = id,
    content = content,
    timestamp = formatDateTime(),
    isAI = isAI
)