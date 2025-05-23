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
import com.seravian.feat_chat.data.AudioStreamer
import com.seravian.feat_chat.domain.repository.ChatRepository
import com.seravian.feat_chat.presentation.viewModel.chat.ChatAction
import com.seravian.feat_chat.presentation.viewModel.chat.ChatState
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceAction
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceState
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

    private val _voiceState: MutableStateFlow<VoiceState> = MutableStateFlow(VoiceState())
    val voiceState = _voiceState.asStateFlow()

    private lateinit var audioStreamer: AudioStreamer
    private lateinit var audioPlayer: AudioPlayer
    private var isInVoiceMode: Boolean = false

    private var messageResponsesCollection: Job ?= null
    private var audioResponseCollection: Job ?= null
    private var messagesCollection: Job ?= null

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
            else -> {}
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
            is VoiceAction.NavigateBack -> { isInVoiceMode = false }
            else -> {}
        }
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            chatRepository.getSignalRConnectionStatus().collect { status ->
                when(status) {
                    ConnectionStatus.CONNECTING -> {
                        buildAudioPlayer()
                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatState.value.joinChatResult == null) {
                            joinChat(_chatState.value.currentChat?.id ?: "")
                            getChatMessages(_chatState.value.currentChat?.id ?: "")
                            collectMessageResponses()
                            if (::audioPlayer.isInitialized && isInVoiceMode) {
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
        messageResponsesCollection = viewModelScope.launch {
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
        messageResponsesCollection?.start()
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

                        if (result.second.isNotEmpty()) {
                            val lastMessage = result.second.last()
                            if (isInVoiceMode && ::audioPlayer.isInitialized
                                && lastMessage.id.first != _voiceState.value.lastAudioId
                                && lastMessage.isAI
                                && lastMessage.messageType == MessageType.VOICE_MODE_TEXT
                                && lastMessage.isNotOlderThan(2)
                            ) {
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
        messageResponsesCollection?.cancel()
        messageResponsesCollection = null
        messagesCollection?.cancel()
        messagesCollection = null
    }

    //////////////////////////////////
    ///////// VOICE MODE METHODS
    /////////////////////////////////

    private fun startStreaming() {
        if (!::audioStreamer.isInitialized) {
            audioStreamer = AudioStreamer(
                viewModelScope,
                onCapturingComplete = { capturedVoice ->
                    _voiceState.update {
                        it.copy(
                            isStreamingVoice = false,
                        )
                    }
                    _voiceState.update {
                        it.copy(
                            voiceUploadResult = chatRepository.sendCapturedVoice(capturedVoice)
                        )
                    }
                },
                onVoiceDetected = {
                    if (::audioPlayer.isInitialized) audioPlayer.stop()
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
        if (!_voiceState.value.isStreamingVoice &&
            (_voiceState.value.voiceUploadResult == null ||
            _voiceState.value.receivedAIAudioResult != null)) {
            _voiceState.update {
                it.copy(
                    isStreamingVoice = true
                )
            }
            audioStreamer.start()
        }
    }

    private fun collectAudioResponse() {
        audioResponseCollection = viewModelScope.launch {
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
        audioResponseCollection?.start()
    }

    private fun buildAudioPlayer() {
        if (!::audioPlayer.isInitialized) {
            audioPlayer = AudioPlayer(
                viewModelScope,
                onPlayBackStarted = { audioId ->
                    _voiceState.update {
                        it.copy(
                            lastAudioId = audioId,
                            voiceUploadResult = null
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

    private fun stopAudioResponseCollection(stopAudioPlayer: Boolean = false) {
        audioResponseCollection?.cancel()
        audioResponseCollection = null

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