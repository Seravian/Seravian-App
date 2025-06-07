package com.seravian.feat_chat.data.repository

import android.util.Log
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.ErrorType
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.domain.models.Chat
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatBotStateRepository(
    private val seravianChatBotDataSource: ChatBotRemoteDataSource
) {
    private val _chatBotState = MutableStateFlow(ChatBotState())
    val chatBotState = _chatBotState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val connectionStatusSharedFlow: SharedFlow<ConnectionStatus> =
        seravianChatBotDataSource.getSignalRConnectionStatus()
            .shareIn(scope, SharingStarted.Lazily, replay = 1)

    private var jobInternalConnectionStatus: Job? = null

    fun updateCurrentChat(newChat: Chat?) {
        _chatBotState.update {
            it.copy(
                previousChat = it.currentChat ?: it.previousChat,
                currentChat = newChat
            ).also {
                if (it.currentChat != it.previousChat) {
                    it.copy(
                        isWaitingForResponse = false
                    )
                }
            }
        }
    }

    fun updateLastMessage(message: Message) {
        _chatBotState.update {
            it.copy(
                lastMessage = message
            )
        }
    }

    fun changeResponseWaiting() {
        _chatBotState.update {
            it.copy(
                isWaitingForResponse = !it.isWaitingForResponse
            )
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
            connectionStatusSharedFlow.collect { status ->
                when (status) {
                    ConnectionStatus.CONNECTING -> {

                    }
                    ConnectionStatus.CONNECTED -> {
                        if (_chatBotState.value.joinChatResult == null || _chatBotState.value.joinChatResult is NetworkResult.Error) {
                            joinChat(JoinChatRequest(_chatBotState.value.currentChat?.id ?: ""))
                        }
                    }
                    ConnectionStatus.RECONNECTING -> {

                    }
                    ConnectionStatus.DISCONNECTED -> {
                        _chatBotState.update {
                            it.copy(
                                joinChatResult = null
                            )
                        }
                    }
                    ConnectionStatus.IDLE -> {

                    }
                }
            }
        }
    }

    suspend fun joinChat(joinChatRequest: JoinChatRequest) {
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
}