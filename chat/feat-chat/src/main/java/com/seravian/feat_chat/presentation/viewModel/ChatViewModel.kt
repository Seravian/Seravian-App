package com.seravian.feat_chat.presentation.viewModel

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
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.domain.MessageType
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.data.AudioPlayer
import com.seravian.feat_chat.data.VoiceRecorder
import com.seravian.feat_chat.domain.repository.ChatRepository
import com.seravian.feat_chat.presentation.viewModel.chat.ChatAction
import com.seravian.feat_chat.presentation.viewModel.chat.ChatState
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceAction
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ChatViewModel(
    private val chatRepository: ChatRepository
): BaseViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState())
    val chatState = _chatState.asStateFlow()

    private val _voiceState: MutableStateFlow<VoiceState> = MutableStateFlow(VoiceState())
    val voiceState = _voiceState.asStateFlow()

    private val voiceRecorder: VoiceRecorder = buildVoiceRecorder()
    private val audioPlayer: AudioPlayer = buildAudioPlayer()
    private var isInVoiceMode: Boolean = false

    private var jobMessageResponsesCollection: Job ?= null
    private var jobAudioResponseCollection: Job ?= null
    private var jobMessagesCollection: Job ?= null

    fun chatAction(action: ChatAction) {
        when(action) {
            is ChatAction.CreateChat -> createChat()
            is ChatAction.DeleteChat -> deleteChat(action.chatId)
            is ChatAction.EditChat -> editChat(action.chatId, action.title)
            ChatAction.GetChats -> getChats()
            is ChatAction.JoinChat -> startConnection()
            is ChatAction.GetChatMessages -> getChatMessages(action.chatId)
            ChatAction.LeaveChat -> leaveChat()
            is ChatAction.SendMessage -> sendRequest(action.message)
            is ChatAction.ClearChatResults -> clearChatResults()
            is ChatAction.StopMessageCollections -> stopMessageCollections()
            is ChatAction.NavigateToVoiceMode -> { isInVoiceMode = true }
            ChatAction.NavigateBack -> {  }
        }
    }

    fun voiceAction(action: VoiceAction) {
        when(action) {
            is VoiceAction.StartStreaming -> startStreaming()
            is VoiceAction.StartCollectingAIAudio -> collectAudioResponse()
            is VoiceAction.ChangeMicState -> changeMicState()
            is VoiceAction.BuildAudioPlayer -> buildAudioPlayer()
            is VoiceAction.StopStreaming -> stopStreaming()
            is VoiceAction.StopCollectingAIAudio -> stopAudioResponseCollection(action.stopAudioPlayer)
            is VoiceAction.ResetVoiceState -> resetVoiceState()
            is VoiceAction.RestartStreaming -> restartStreaming()
            is VoiceAction.NavigateBack -> {
                isInVoiceMode = false
                stopStreaming()
            }
        }
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            chatRepository.getSignalRConnectionStatus().collect { status ->
                when(status) {
                    ConnectionStatus.CONNECTING -> {

                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatState.value.joinChatResult == null) {
                            joinChat(_chatState.value.currentChat?.id ?: "")
                            getChatMessages(_chatState.value.currentChat?.id ?: "")
                            collectMessageResponses()
                            if (isInVoiceMode) {
                                collectAudioResponse()
                            }
                            _chatState.update {
                                it.copy(
                                    joinChatResult = NetworkResult.Success(Unit)
                                )
                            }
                        }
                    }
                    ConnectionStatus.RECONNECTING -> {

                    }
                    ConnectionStatus.DISCONNECTED -> {
                        stopMessageCollections()
                        stopAudioResponseCollection()
                        _chatState.update {
                            it.copy(
                                joinChatResult = null
                            )
                        }
                    }
                    ConnectionStatus.IDLE -> { Log.d("Status", "IDLE") }
                }
            }
        }
    }

    //////////////////////////////////
    ///////// MESSAGES MODE METHODS
    /////////////////////////////////

    private fun collectMessageResponses() {
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

    private fun startConnection() {
        viewModelScope.launch {
            chatRepository.startConnection()
        }.invokeOnCompletion { collectConnectionStatus() }
    }

    private fun createChat() {
        viewModelScope.launch {
            val result = chatRepository.createChat(CreateChatRequest())
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

                        if (result.second.isNotEmpty()) {
                            val lastMessage = result.second.last()
                            if (!isInVoiceMode && lastMessage.messageType == MessageType.VOICE_MODE_TEXT) {
                                _voiceState.update { it.copy(
                                    voiceUploadResult = null,
                                    receivedAIAudioResult = null
                                ) }
                            }
                            if (shouldGetVoiceMessage(lastMessage)) {
                                _voiceState.update {
                                    it.copy(
                                        isWaitingForResponse = true
                                    )
                                }

                                val audioResult = chatRepository.fetchAIAudio(
                                    FetchAIAudioRequest(lastMessage.id.first ?: -1)
                                )

                                _voiceState.update {
                                    it.copy(
                                        receivedAIAudioResult = audioResult
                                    )
                                }

                                audioResult.onSuccess { audio ->
                                    audioPlayer.play(audio)
                                }
                            }
                        }
                    }
                    .onError {
                        _chatState.update { it.copy(
                            getChatMessagesResult = messagesResult
                        ) }
                    }
            }
        }
    }

    private fun shouldGetVoiceMessage(lastMessage: Message) = (isInVoiceMode
            && lastMessage.id.first != _voiceState.value.lastAudioId
            && lastMessage.isAI
            && lastMessage.messageType == MessageType.VOICE_MODE_TEXT
            && lastMessage.isNotOlderThan(2))

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

    private fun stopMessageCollections() {
        jobMessageResponsesCollection?.cancel()
        jobMessageResponsesCollection = null
        jobMessagesCollection?.cancel()
        jobMessagesCollection = null
    }

    //////////////////////////////////
    ///////// VOICE MODE METHODS
    /////////////////////////////////

    private fun startStreaming() {
        if (shouldStartRecording()) {
            viewModelScope.launch {
                _voiceState.update {
                    it.copy(
                        isStreamingVoice = true
                    )
                }
                voiceRecorder.start()
            }
        }
    }

    private fun shouldStartRecording() = !_voiceState.value.isStreamingVoice &&
            !_voiceState.value.isWaitingForResponse

    private fun buildVoiceRecorder(): VoiceRecorder {
        return VoiceRecorder(
            onCapturingComplete = { capturedVoice ->
                _voiceState.update {
                    it.copy(
                        isStreamingVoice = false,
                        isWaitingForResponse = true
                    )
                }

                viewModelScope.launch {
                    val uploadAudioResult = withContext(Dispatchers.IO) {
                        chatRepository.sendCapturedVoice(capturedVoice)
                    }
                    _voiceState.update {
                        it.copy(
                            voiceUploadResult = uploadAudioResult
                        )
                    }
                }
            },
            onVoiceDetected = {
                audioPlayer.stop()
                _voiceState.update {
                    it.copy(
                        voiceUploadResult = null,
                        receivedAIAudioResult = null
                    )
                }
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

    private fun collectAudioResponse() {
        jobAudioResponseCollection = viewModelScope.launch {
            chatRepository.receiveAIAudioResponse { audioResult ->
                _voiceState.update {
                    it.copy(
                        receivedAIAudioResult = audioResult
                    )
                }

                audioResult.onSuccess { audio ->
                    audioPlayer.play(audio)
                }
            }
        }
    }

    private fun buildAudioPlayer(): AudioPlayer {
        return AudioPlayer(
            onPlayBackStarted = { audioId ->
                _voiceState.update {
                    it.copy(
                        lastAudioId = audioId,
                        voiceUploadResult = null,
                        isWaitingForResponse = false
                    )
                }
                startStreaming()
            },
            onPlaybackComplete = {
                _voiceState.update {
                    it.copy(
                        receivedAIAudioResult = null
                    )
                }
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

    private fun changeMicState() {
        _voiceState.update {
            it.copy(
                isMuted = !_voiceState.value.isMuted
            )
        }.also {
            if (_voiceState.value.isMuted) voiceRecorder.stop() else voiceRecorder.start()
        }
    }

    private fun stopStreaming() {
        _voiceState.update {
            it.copy(
                isStreamingVoice = false
            )
        }

        voiceRecorder.stop()
    }

    private fun stopAudioResponseCollection(stopAudioPlayer: Boolean = false) {
        jobAudioResponseCollection?.cancel()
        jobAudioResponseCollection = null

        if (stopAudioPlayer) {
            audioPlayer.stop()
        }
    }

    private fun resetVoiceState() {
        _voiceState.update { VoiceState() }
    }

    private fun restartStreaming() {
        _voiceState.update {
            it.copy(
                voiceUploadResult = null,
                receivedAIAudioResult = null
            )
        }
        startStreaming()
    }
}