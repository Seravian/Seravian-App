package com.seravian.feat_chat.presentation.viewModel.chat

import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private var jobConnectionStatusCollection: Job ?= null
    private var jobMessageResponsesCollection: Job ?= null
    private var jobMessagesCollection: Job ?= null

    init {
        collectConnectionStatus()

        viewModelScope.launch {
            chatBotStateRepository.checkResponseProcessing()
        }
    }

    fun chatAction(action: ChatAction) {
        when(action) {
            is ChatAction.GetChatMessages -> getChatMessages(action.chatId)
            ChatAction.LeaveChat -> leaveChat()
            is ChatAction.SendMessage -> sendRequest(action.message)
            is ChatAction.StopMessageCollections -> stopMessageCollections()
            is ChatAction.NavigateToVoiceMode -> {
                chatBotStateRepository.updateLastMessage(_chatState.value.messagesList.last())
            }
        }
    }

    private fun collectConnectionStatus() {
        jobConnectionStatusCollection = viewModelScope.launch {
            chatBotStateRepository.chatBotState.collect { newState ->
                _chatState.update {
                    it.copy(
                        currentChat = newState.currentChat,
                        isWaitingForResponse = newState.isWaitingForResponse,
                        joinChatResult = newState.joinChatResult
                    )
                }

                when(newState.joinChatResult) {
                    is NetworkResult.Success -> {
                        getChatMessages(chatBotStateRepository.chatBotState.value.currentChat?.id ?: "")
                        collectMessageResponses()
                    }

                    is NetworkResult.Error -> {
                        stopMessageCollections()
                    }

                    null -> {
                        chatBotStateRepository.startConnection()
                    }
                }
            }
        }
    }

    private fun collectMessageResponses() {
        if (jobMessageResponsesCollection != null) return
        jobMessageResponsesCollection = viewModelScope.launch {
            chatRepository.receiveClientResponse()

            chatRepository.receiveAIResponse()

            chatRepository.receiveMessageConfirmation { confirmation ->
                chatRepository.insertConfirmedMessage(
                    _chatState.value.messagesList.find { message ->
                        message.id.second == confirmation.clientMessageId
                    }?.copy(
                        id = Pair(confirmation.messageId, null),
                        timestamp = confirmation.timestampUtc
                    ) ?: Message()
                )
            }
        }
    }

    private fun getChatMessages(chatId: String) {
        if (jobMessagesCollection != null) return
        jobMessagesCollection = viewModelScope.launch {
            val messagesFlow = chatRepository.getChatMessages(GetChatMessagesRequest(chatId))
            messagesFlow.collect { messagesResult ->
                messagesResult
                    .onSuccess { result ->
                        _chatState.update { it.copy(
                            currentChat = result.first,
                            messagesList = result.second,
                            getChatMessagesResult = messagesResult
                        ) }
                        chatBotStateRepository.checkResponseProcessing()
                    }
                    .onError {
                        _chatState.update { it.copy(
                            getChatMessagesResult = messagesResult
                        ) }
                    }
            }
        }
    }

    private fun leaveChat() {
        stopMessageCollections()
        jobConnectionStatusCollection?.cancel()
        jobConnectionStatusCollection = null
        viewModelScope.launch(Dispatchers.IO) {
            chatBotStateRepository.stopConnection()
        }
    }

    private fun sendRequest(message: String) {
        viewModelScope.launch {
            val clientRequest = ClientRequest(
                messageClientId = UUID.randomUUID().toString(),
                message = message
            )
            _chatState.update { it.copy(
                messagesList = it.messagesList + clientRequest.buildMessage()
            ) }
            chatRepository.sendRequest(clientRequest)
        }.invokeOnCompletion {

        }
    }

    private fun stopMessageCollections() {
        jobMessageResponsesCollection?.cancel()
        jobMessageResponsesCollection = null
        jobMessagesCollection?.cancel()
        jobMessagesCollection = null
    }
}