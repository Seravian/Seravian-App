package com.seravian.feat_chat.presentation.viewModel.voice

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.presentation.BaseViewModel
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.domain.MessageType
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.data.repository.ChatBotStateRepository
import com.seravian.feat_chat.data.utils.AudioPlayer
import com.seravian.feat_chat.data.utils.VoiceRecorder
import com.seravian.feat_chat.domain.repository.VoiceModeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class VoiceModeViewModel(
    private val voiceModeRepository: VoiceModeRepository,
    private val chatBotStateRepository: ChatBotStateRepository
): BaseViewModel() {
    private val _voiceState: MutableStateFlow<VoiceState> = MutableStateFlow(VoiceState())
    val voiceState = _voiceState.asStateFlow()

    private val voiceRecorder: VoiceRecorder = buildVoiceRecorder()
    private val audioPlayer: AudioPlayer = buildAudioPlayer()

    private var jobAudioResponseCollection: Job ?= null

    init {
        _voiceState.update {
            it.copy(
                isWaitingForResponse = chatBotStateRepository.chatBotState.value.isWaitingForResponse,
                lastAudioId = chatBotStateRepository.chatBotState.value.lastMessage?.id?.first,
                currentChat = chatBotStateRepository.chatBotState.value.currentChat
            )
        }
        collectConnectionStatus()
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
            is VoiceAction.NavigateBack -> { stopStreaming() }
        }
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            chatBotStateRepository.connectionStatus().collect { status ->
                when(status) {
                    ConnectionStatus.CONNECTING -> {

                    }
                    ConnectionStatus.CONNECTED -> {
                        collectAudioResponse()
                    }
                    ConnectionStatus.RECONNECTING -> {
                        getLastAudioResponse()
                    }
                    ConnectionStatus.DISCONNECTED -> {
                        stopAudioResponseCollection()
                    }
                    ConnectionStatus.IDLE -> { Log.d("Status", "IDLE") }
                }
            }
        }
    }

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
            !chatBotStateRepository.chatBotState.value.isWaitingForResponse

    private fun getLastAudioResponse() {
        val lastMessage = chatBotStateRepository.chatBotState.value.lastMessage
        _voiceState.update {
            it.copy(
                lastAudioId = lastMessage?.id?.first
            )
        }

        if (shouldGetVoiceMessage(lastMessage)) {
            _voiceState.update {
                it.copy(
                    voiceUploadResult = null,
                    receivedAIAudioResult = null
                )
            }

            _voiceState.update {
                it.copy(
                    isWaitingForResponse = chatBotStateRepository.changeResponseWaiting()
                )
            }

            val audioResult = runBlocking {
                voiceModeRepository.fetchAIAudio(
                    FetchAIAudioRequest(lastMessage?.id?.first ?: -1)
                )
            }

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

    private fun shouldGetVoiceMessage(lastMessage: Message?): Boolean {
        lastMessage?.let {
            return !chatBotStateRepository.chatBotState.value.isWaitingForResponse
                    && lastMessage.id.first != _voiceState.value.lastAudioId
                    && lastMessage.isAI
                    && lastMessage.messageType == MessageType.VOICE_MODE_TEXT
                    && lastMessage.isNotOlderThan(2)
        }

        return false
    }

    private fun buildVoiceRecorder(): VoiceRecorder {
        return VoiceRecorder(
            onCapturingComplete = { capturedVoice ->
                _voiceState.update {
                    it.copy(
                        isStreamingVoice = false
                    )
                }
                _voiceState.update {
                    it.copy(
                        isWaitingForResponse = chatBotStateRepository.changeResponseWaiting()
                    )
                }

                viewModelScope.launch {
                    val uploadAudioResult = withContext(Dispatchers.IO) {
                        voiceModeRepository.sendCapturedVoice(
                            capturedVoice,
                            chatBotStateRepository.chatBotState.value.currentChat?.id ?: ""
                        )
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
            voiceModeRepository.receiveAIAudioResponse { audioResult ->
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
                        voiceUploadResult = null
                    )
                }
                _voiceState.update {
                    it.copy(
                        isWaitingForResponse = chatBotStateRepository.changeResponseWaiting()
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