package com.seravian.feat_chat.presentation.viewModel

import android.util.Base64
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.data.AudioStreamer
import com.seravian.feat_chat.domain.repository.ChatRepository
import com.seravian.feat_chat.presentation.viewModel.chat.ChatAction
import com.seravian.feat_chat.presentation.viewModel.chat.ChatState
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceAction
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val chatRepository: ChatRepository
): BaseViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private val _voiceState: MutableStateFlow<VoiceState> = MutableStateFlow(VoiceState())
    val voiceState = _voiceState.asStateFlow()

    private lateinit var audioStreamer: AudioStreamer
    private var responseCollection: Job ?= null
    private var messagesCollection: Job ?= null

    fun chatAction(action: ChatAction) {
        when(action) {
            is ChatAction.CreateChat -> createChat(action.title)
            is ChatAction.DeleteChat -> deleteChat(action.chatId)
            is ChatAction.EditChat -> editChat(action.chatId, action.title)
            ChatAction.GetChats -> getChats()
            is ChatAction.JoinChat -> startConnection()
            is ChatAction.GetChatMessages -> getChatMessages(action.chatId)
            ChatAction.LeaveChat -> leaveChat()
            is ChatAction.SendMessage -> sendRequest(action.message)
            is ChatAction.ClearChatResults -> clearChatResults()
            is ChatAction.StopCollections -> stopCollections()
            else -> {}
        }
    }

    fun voiceAction(action: VoiceAction) {
        when(action) {
            is VoiceAction.StartStreaming -> startStreaming()
            is VoiceAction.ChangeMicState -> changeMicState()
            is VoiceAction.StopStreaming -> stopStreaming()
            is VoiceAction.ResetVoiceState -> resetVoiceState()
            else -> {}
        }
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            chatRepository.getSignalRConnectionStatus().collectLatest { status ->
                when(status) {
                    ConnectionStatus.CONNECTING -> {
                        _chatState.update {
                            it.copy(
                                joinChatResult = null
                            )
                        }
                        stopCollections()
                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatState.value.joinChatResult == null) {
                            joinChat(_chatState.value.currentChat?.id ?: "")
                            getChatMessages(_chatState.value.currentChat?.id ?: "")
                            collectResponses()
                            _chatState.update {
                                it.copy(
                                    joinChatResult = NetworkResult.Success(Unit)
                                )
                            }
                        }
                    }
                    ConnectionStatus.RECONNECTING -> {
                        stopCollections()
                        _chatState.update {
                            it.copy(
                                joinChatResult = null
                            )
                        }
                    }
                    ConnectionStatus.DISCONNECTED -> {

                    }
                    else -> { Log.d("Status", "IDLE") }
                }
            }
        }
    }

    private fun collectResponses() {
        responseCollection = viewModelScope.launch {
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
        responseCollection?.start()
    }

    private fun startConnection() {
        viewModelScope.launch {
            chatRepository.startConnection()
        }.invokeOnCompletion { collectConnectionStatus() }
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
        messagesCollection = viewModelScope.launch {
            val messagesFlow = chatRepository.getChatMessages(GetChatMessagesRequest(chatId))
            messagesFlow.collect { messagesResult ->
                messagesResult
                    .onSuccess { result ->
                        _chatState.update { it.copy(
                            currentChat = result.first,
                            messagesList = result.second,
                            getChatMessagesResult = messagesResult
                        ) }
                    }
                    .onError {
                        _chatState.update { it.copy(
                            getChatMessagesResult = messagesResult
                        ) }
                    }
            }
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            val chatsFlow = chatRepository.getChats()
            chatsFlow.collect { chatsResult ->
                chatsResult
                    .onSuccess { result ->
                        _chatState.update { it.copy(
                            chatsList = result,
                            getChatsResult = chatsResult
                        ) }
                    }
                    .onError {
                        _chatState.update { it.copy(
                            getChatsResult = chatsResult
                        ) }
                    }
            }
        }
    }

    private fun joinChat(chatId: String) {
        viewModelScope.launch { chatRepository.joinChat(JoinChatRequest(chatId)) }
    }

    private fun leaveChat() {
        viewModelScope.launch {
            chatRepository.stopConnection()
            _chatState.update { it.copy(
                currentChat = null,
                messagesList = emptyList(),
                joinChatResult = null
            ) }
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
        }
    }

    private fun startStreaming() {
        if (!::audioStreamer.isInitialized) {
            audioStreamer = AudioStreamer(
                viewModelScope,
                onCapturingComplete = { capturedVoice ->
                    _voiceState.update {
                        it.copy(
                            isStreamingVoice = false
                        )
                    }
                    chatRepository.sendCapturedVoice(capturedVoice)
                },
                onAmplitudeUpdate = { amplitude ->
                    _voiceState.update {
                        it.copy(
                            voiceAmplitude = amplitude
                        )
                    }
                }
            )
        }
        _voiceState.update {
            it.copy(
                isStreamingVoice = true
            )
        }
        audioStreamer.start()
    }

    private fun changeMicState() {
        _voiceState.update {
            it.copy(
                isMuted = !_voiceState.value.isMuted
            )
        }.also {
            if (_voiceState.value.isMuted) audioStreamer.stop() else audioStreamer.start()
        }
    }

    private fun stopStreaming() {
        _voiceState.update {
            it.copy(
                isStreamingVoice = false
            )
        }

        if (::audioStreamer.isInitialized) {
            audioStreamer.stop()
        }
    }

    private fun resetVoiceState() {
        _voiceState.update { VoiceState() }
    }

    private fun clearChatResults() {
        _chatState.update {
            it.copy(
                createChatResult = null,
                deleteChatResult = null,
                editChatResult = null,
                getChatMessagesResult = null
            )
        }
    }

    private fun stopCollections() {
        responseCollection?.cancel()
        responseCollection = null
        messagesCollection?.cancel()
        messagesCollection = null
    }
}