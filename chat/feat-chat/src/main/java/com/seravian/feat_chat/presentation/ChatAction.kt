package com.seravian.feat_chat.presentation

sealed interface ChatAction {
    data class CreateChat(val title: String) : ChatAction
    data class EditChat(val chatId: String, val title: String) : ChatAction
    data class DeleteChat(val chatId: String) : ChatAction
    data object GetChats : ChatAction
    data class GetChatMessages(val chatId: String) : ChatAction
    data class JoinChat(val chatId: String) : ChatAction
    data object LeaveChat : ChatAction
    data class SendMessage(val message: String) : ChatAction
    data object ClearChatResults : ChatAction
}