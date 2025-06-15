package com.seravian.feat_chat.data.repository

import android.util.Log
import com.seravian.core_network.data.ConnectionStatus
import com.seravian.core_network.data.ErrorType
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_chat.data.dto.request.chat.IsProcessingRequest
import com.seravian.core_chat.data.dto.request.chat.JoinChatRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCheckRequest
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Diagnosis
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatBotStateRepository(
    private val seravianChatBotDataSource: ChatBotRemoteDataSource
) {
    private val _chatBotState = MutableStateFlow(ChatBotState())
    val chatBotState = _chatBotState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var jobInternalConnectionStatus: Job? = null

    fun updateCurrentChat(newChat: Chat?) {
        _chatBotState.update {
            it.copy(
                currentChat = newChat
            )
        }
    }

    fun updateLastMessage(message: Message?) {
        _chatBotState.update {
            it.copy(
                lastMessage = message
            )
        }
    }

    fun updateCurrentDiagnosis(diagnosis: Diagnosis?) {
        _chatBotState.update {
            it.copy(
                currentDiagnosis = diagnosis
            )
        }
    }

    suspend fun checkResponseProcessing() {
        val isWaitingForResponse = seravianChatBotDataSource.isProcessing(
            IsProcessingRequest(_chatBotState.value.currentChat?.id ?: "")
        )
        isWaitingForResponse.onSuccess { response ->
            _chatBotState.update {
                it.copy(
                    isWaitingForResponse = response.isProcessing
                )
            }
        }
    }

    suspend fun startConnection() {
        seravianChatBotDataSource.startSignalRConnection()
        collectConnectionStatusInternally()
    }

    suspend fun stopConnection() {
        seravianChatBotDataSource.stopSignalRConnection()
    }

    private fun collectConnectionStatusInternally() {
        if (jobInternalConnectionStatus != null) return

        jobInternalConnectionStatus = scope.launch {
            seravianChatBotDataSource.getSignalRConnectionStatus().collect { status ->
                when (status) {
                    ConnectionStatus.CONNECTING -> {

                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatBotState.value.joinChatResult == null || _chatBotState.value.joinChatResult is NetworkResult.Error) {
                            joinChat(JoinChatRequest(_chatBotState.value.currentChat?.id ?: ""))
                            observeDiagnoses()
                        }
                    }
                    ConnectionStatus.RECONNECTING -> {

                    }
                    ConnectionStatus.DISCONNECTED -> {
                        leaveChat()
                    }
                    ConnectionStatus.IDLE -> {

                    }
                }
            }
        }
    }

    private suspend fun joinChat(joinChatRequest: JoinChatRequest) {
        try {
            seravianChatBotDataSource.joinChat(joinChatRequest)
            _chatBotState.update {
                it.copy(
                    joinChatResult = NetworkResult.Success(Unit)
                )
            }
        } catch (e: Exception) {
            Log.e("Chat", "Error joining chat: $e")
            _chatBotState.update {
                it.copy(
                    joinChatResult = NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR))
                )
            }
        }
    }

    fun leaveChat() {
        _chatBotState.update {
            it.copy(
                joinChatResult = null
            )
        }
    }

    private fun observeDiagnoses() {
        seravianChatBotDataSource.receiveDiagnosisReadyResponse { readyResponse ->
            _chatBotState.update {
                it.copy(
                    lastNotifiedDiagnosisId = readyResponse.id,
                    isWaitingForDiagnosis = false
                )
            }
        }
    }

    suspend fun checkDiagnosis() {
        val checkingResult = seravianChatBotDataSource.isDiagnosing(
            DiagnosisCheckRequest(_chatBotState.value.currentChat?.id ?: "")
        )

        checkingResult
            .onSuccess { response ->
                _chatBotState.update {
                    it.copy(
                        isWaitingForDiagnosis = response.isDiagnosing
                    )
                }
            }
            .onError {
                checkDiagnosis()
            }
    }
}