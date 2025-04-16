package com.seravian.core_chat.domain.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    var id: String = "",
    val senderName: String = "",
    val senderId: String = "",
    val content: String = "",
    val dateTime: Long = 0
) {
    fun formatDateTime(): String{
        val date = Date(dateTime)
        val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}
