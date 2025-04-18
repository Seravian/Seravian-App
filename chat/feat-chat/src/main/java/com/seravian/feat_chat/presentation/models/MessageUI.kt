package com.seravian.feat_chat.presentation.models

import com.seravian.core_chat.domain.models.Message

data class MessageUI(
    var id: String = "",
    val senderName: String = "",
    val senderId: String = "",
    val content: String = "",
    val dateTime: String = ""
)

fun Message.toMessageUI() = MessageUI(
    id = id,
    senderName = senderName,
    senderId = senderId,
    content = content,
    dateTime = formatDateTime()
)