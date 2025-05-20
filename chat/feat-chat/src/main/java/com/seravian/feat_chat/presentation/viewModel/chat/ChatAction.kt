package com.seravian.feat_chat.presentation.viewModel.chat

sealed interface ChatAction {
    data object CreateChat : ChatAction
    data class EditChat(val chatId: String, val title: String) : ChatAction
    data class DeleteChat(val chatId: String) : ChatAction
    data object GetChats : ChatAction
    data object JoinChat : ChatAction
    data class GetChatMessages(val chatId: String) : ChatAction
    data object LeaveChat : ChatAction
    data class SendMessage(val message: String) : ChatAction
    data object NavigateToVoiceMode : ChatAction
    data object NavigateBack : ChatAction
    data object ClearChatResults : ChatAction
    data object StopCollections : ChatAction
}