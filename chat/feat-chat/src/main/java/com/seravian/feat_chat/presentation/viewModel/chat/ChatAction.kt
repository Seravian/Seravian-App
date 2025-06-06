package com.seravian.feat_chat.presentation.viewModel.chat

sealed interface ChatAction {
    data class GetChatMessages(val chatId: String) : ChatAction
    data object LeaveChat : ChatAction
    data class SendMessage(val message: String) : ChatAction
    data object NavigateToVoiceMode : ChatAction
    data object NavigateBack : ChatAction
    data object StopMessageCollections : ChatAction
}