package com.seravian.feat_chat.presentation.models

import com.seravian.core_chat.domain.models.Chat

data class ChatUI(
    val id: String,
    val title: String,
    val createdAt: String
)

fun Chat.toChatUI() = ChatUI(
    id = id,
    title = title ?: "Seravian",
    createdAt = formatDateTime()
)