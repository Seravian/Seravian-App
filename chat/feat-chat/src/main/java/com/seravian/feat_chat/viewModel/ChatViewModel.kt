package com.seravian.feat_chat.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.domain.entity.Message
import com.seravian.core_chat.domain.entity.Room

class ChatViewModel () : BaseViewModel() {

    val messageState = mutableStateOf("")
    val messagesListState = mutableStateListOf<Message>()
    var room: Room? = null

}