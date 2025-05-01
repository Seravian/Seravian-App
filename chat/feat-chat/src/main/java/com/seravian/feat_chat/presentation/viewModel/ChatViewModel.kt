package com.seravian.feat_chat.presentation.viewModel

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.feat_chat.domain.ChatRepository
import com.seravian.feat_chat.presentation.ChatAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
): BaseViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    fun chatAction(action: ChatAction) {
        when(action) {
            is ChatAction.CreateChat -> createChat(action.title)
            is ChatAction.DeleteChat -> deleteChat(action.chatId)
            is ChatAction.EditChat -> editChat(action.chatId, action.title)
            ChatAction.GetChats -> getChats()
            is ChatAction.GetChatMessages -> getChatMessages(action.chatId)
            is ChatAction.JoinChat -> TODO()
            ChatAction.LeaveChat -> TODO()
            is ChatAction.SendMessage -> TODO()
        }
    }

    private fun createChat(title: String) {
        viewModelScope.launch {
            val result = chatRepository.createChat(CreateChatRequest(title))
            _chatState.update { it.copy(createChatResult = result) }
        }
    }

    private fun deleteChat(chatId: String) {
        viewModelScope.launch {
            val result = chatRepository.deleteChat(DeleteChatRequest(chatId))
            _chatState.update { it.copy(deleteChatResult = result) }
        }
    }

    private fun editChat(chatId: String, title: String) {
        viewModelScope.launch {
            val result = chatRepository.updateChat(EditChatRequest(chatId, title))
            _chatState.update { it.copy(editChatResult = result) }
        }
    }

    private fun getChatMessages(chatId: String) {
        viewModelScope.launch {
            val result = chatRepository.getChatMessages(GetChatMessagesRequest(chatId))
            result.onSuccess { chatMessagesResponse ->
                _chatState.update {
                    it.copy(messagesList = chatMessagesResponse.messages.map { chatMessage ->
                        chatMessage.extractMessage()
                    }.toMutableList())
                }
            }
            _chatState.update { it.copy(getChatMessagesResult = result) }
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            val result = chatRepository.getChats()
            result.onSuccess { chatResponses ->
                _chatState.update {
                    it.copy(chatsList = chatResponses.map { chatResponse ->
                        chatResponse.extractChat()
                    })
                }
            }
            _chatState.update { it.copy(getChatsResult = result) }
        }
    }
}