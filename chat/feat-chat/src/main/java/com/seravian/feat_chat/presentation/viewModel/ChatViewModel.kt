package com.seravian.feat_chat.presentation.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.ConnectionStatus
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.domain.ChatRepository
import com.seravian.feat_chat.presentation.ChatAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val chatRepository: ChatRepository
): BaseViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private var responseCollection: Job? = null

    fun chatAction(action: ChatAction) {
        when(action) {
            is ChatAction.CreateChat -> createChat(action.title)
            is ChatAction.DeleteChat -> deleteChat(action.chatId)
            is ChatAction.EditChat -> editChat(action.chatId, action.title)
            ChatAction.GetChats -> getChats()
            is ChatAction.GetChatMessages -> getChatMessages(action.chatId)
            ChatAction.LeaveChat -> leaveChat()
            is ChatAction.SendMessage -> sendRequest(action.message)
            is ChatAction.ClearChatResults -> clearChatResults()
            else -> {}
        }
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            chatRepository.getSignalRConnectionStatus().collect { status ->
                when(status) {
                    ConnectionStatus.CONNECTING -> {

                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatState.value.joinChatResult == null || _chatState.value.joinChatResult is NetworkResult.Error) {
                            joinChat(_chatState.value.currentChat?.id ?: "")
                            collectResponses()
                            _chatState.update {
                                it.copy(
                                    joinChatResult = NetworkResult.Success(Unit)
                                )
                            }
                        }
                    }
                    ConnectionStatus.RECONNECTING -> {
                        responseCollection?.cancel()
                        responseCollection = null
                        _chatState.update {
                            it.copy(
                                joinChatResult = null
                            )
                        }
                    }
                    ConnectionStatus.DISCONNECTED -> {
//                        _chatState.update {
//                            it.copy(
//                                currentChat = null,
//                                messagesList = mutableListOf(),
//                                joinChatResult = NetworkResult.Error(NetworkError(ErrorType.SERVER_ERROR))
//                            )
//                        }
                    }
                    else -> { Log.d("Status", "IDLE") }
                }
            }
        }
    }

    private fun updateMessages(updateFunction: (List<Message>) -> List<Message>) {
        _chatState.update { currentState ->
            val updatedList = updateFunction(currentState.messagesList)
            currentState.copy(messagesList = updatedList)
        }
    }

    private fun collectResponses() {
        responseCollection = viewModelScope.launch {
            chatRepository.receiveClientResponse { message ->
                updateMessages { currentList -> currentList + message }
            }

            chatRepository.receiveAIResponse { message ->
                updateMessages { currentList -> currentList + message }
            }

            chatRepository.receiveMessageConfirmation { confirmation ->
                updateMessages { currentList ->
                    currentList.map { message ->
                        if (message.id.second != null && message.id.second == confirmation.clientMessageId) {
                            Message(
                                id = Pair(confirmation.messageId, null),
                                content = message.content,
                                timestamp = confirmation.timestampUtc,
                                isAI = message.isAI
                            )
                        } else {
                            message
                        }
                    }
                }
            }
        }
        responseCollection?.start()
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
                    it.copy(
                        currentChat = chatMessagesResponse.first,
                        messagesList = chatMessagesResponse.second
                    )
                }
                chatRepository.startConnection()
                collectConnectionStatus()
            }
            _chatState.update { it.copy(getChatMessagesResult = result) }
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            val result = chatRepository.getChats()
            result.onSuccess { chats ->
                _chatState.update {
                    it.copy(chatsList = chats)
                }
            }
            _chatState.update { it.copy(getChatsResult = result) }
        }
    }

    private fun joinChat(chatId: String) {
        viewModelScope.launch { chatRepository.joinChat(JoinChatRequest(chatId)) }
    }

    private fun leaveChat() {
        viewModelScope.launch {
            chatRepository.stopConnection()
        }
    }

    private fun sendRequest(message: String) {
        viewModelScope.launch {
            val clientRequest = ClientRequest(
                messageClientId = UUID.randomUUID().toString(),
                message = message
            )
            updateMessages { currentList -> currentList + clientRequest.buildMessage() }
            chatRepository.sendRequest(clientRequest)
        }
    }

    private fun clearChatResults() {
        _chatState.update {
            it.copy(
                createChatResult = null,
                deleteChatResult = null,
                editChatResult = null,
                getChatMessagesResult = null,
                getChatsResult = null
            )
        }
    }
}