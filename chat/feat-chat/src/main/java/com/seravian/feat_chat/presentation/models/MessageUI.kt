package com.seravian.feat_chat.presentation.models

import com.seravian.core_chat.domain.models.Message

data class MessageUI(
    val id: Pair<Long?, String?> = Pair(null, null),
    val content: String = "",
    val timestamp: String = "",
    val isAI: Boolean = false
)

fun Message.toMessageUI() = MessageUI(
    id = id,
    content = content,
    timestamp = formatDateTime(),
    isAI = isAI
)