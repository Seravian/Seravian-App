package com.seravian.feat_chat.presentation.viewModel.chats_list

import com.seravian.core_chat.domain.models.Chat

sealed interface ChatsListAction {
    data object CreateChat : ChatsListAction
    data class EditChat(val chatId: String, val title: String) : ChatsListAction
    data class DeleteChat(val chatId: String) : ChatsListAction
    data object GetChats : ChatsListAction
    data class NavigateToChat(val chat: Chat) : ChatsListAction
    data object ClearChatOperationResults : ChatsListAction
}