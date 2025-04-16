package com.seravian.core_chat.domain.entity

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    var id : String ?= null,
    val senderName : String ?= null,
    val senderId : String ?= null,
    val roomId : String ?= null,
    val content : String ?= null,
    val dateTime : Long ?= null
){
    fun formatDateTime():String{
        val date = Date(dateTime?:0L)
        val simpleDateFormat = SimpleDateFormat("HH:MM A", Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    companion object{
        const val COLLECTION_NAME = "Messages"
    }
}
