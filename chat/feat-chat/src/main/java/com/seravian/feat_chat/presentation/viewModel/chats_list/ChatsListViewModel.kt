package com.seravian.feat_chat.presentation.viewModel.chats_list

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.chat.CreateChatRequest
import com.seravian.core_chat.data.dto.request.chat.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.chat.EditChatRequest
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.ChatsListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatsListViewModel(
    private val chatsListRepository: ChatsListRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _chatsListState = MutableStateFlow(ChatsListState())
    val chatsListState = _chatsListState.asStateFlow()

    fun chatsListAction(action: ChatsListAction) {
        when(action) {
            is ChatsListAction.GetChats -> getChats()
            is ChatsListAction.CreateChat -> createChat()
            is ChatsListAction.DeleteChat -> deleteChat(action.chatId)
            is ChatsListAction.EditChat -> editChat(action.chatId, action.title)
            is ChatsListAction.NavigateToChat -> chatBotStateRepository.updateCurrentChat(action.chat)
            ChatsListAction.ClearChatOperationResults -> clearChatOperationResults()
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            val chatsFlow = chatsListRepository.getChats()
            chatsFlow.collect { chatsResult ->
                chatsResult
                    .onSuccess { result ->
                        _chatsListState.update { it.copy(
                            chatsList = result,
                            getChatsResult = chatsResult
                        ) }
                    }
                    .onError {
                        _chatsListState.update { it.copy(
                            getChatsResult = chatsResult
                        ) }
                    }
            }
        }
    }

    private fun createChat() {
        viewModelScope.launch {
            val result = chatsListRepository.createChat(CreateChatRequest())
            _chatsListState.update { it.copy(createChatResult = result) }
        }
    }

    private fun deleteChat(chatId: String) {
        viewModelScope.launch {
            val result = chatsListRepository.deleteChat(DeleteChatRequest(chatId))
            _chatsListState.update { it.copy(deleteChatResult = result) }
        }
    }

    private fun editChat(chatId: String, title: String) {
        viewModelScope.launch {
            val result = chatsListRepository.updateChat(EditChatRequest(chatId, title))
            _chatsListState.update { it.copy(editChatResult = result) }
        }
    }

    private fun clearChatOperationResults() {
        _chatsListState.update {
            it.copy(
                createChatResult = null,
                deleteChatResult = null,
                editChatResult = null
            )
        }
    }
}