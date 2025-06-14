package com.seravian.feat_chat.data

import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.domain.RealtimeConnection
import com.seravian.core_chat.data.dto.request.message.ClientRequest
import com.seravian.core_chat.data.dto.request.chat.CreateChatRequest
import com.seravian.core_chat.data.dto.request.chat.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.chat.EditChatRequest
import com.seravian.core_chat.data.dto.request.voice.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.chat.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.chat.IsProcessingRequest
import com.seravian.core_chat.data.dto.request.chat.JoinChatRequest
import com.seravian.core_chat.data.dto.request.diagnosis.ChatDiagnosesRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosesDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCheckRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisCreationRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDeletionRequest
import com.seravian.core_chat.data.dto.request.diagnosis.DiagnosisDetailsRequest
import com.seravian.core_chat.data.dto.request.message.SyncMessagesRequest
import com.seravian.core_chat.data.dto.request.voice.UploadVoiceRequest
import com.seravian.core_chat.data.dto.respose.voice.AIAudioReadyResponse
import com.seravian.core_chat.data.dto.respose.voice.AIAudioResponse
import com.seravian.core_chat.data.dto.respose.message.AIResponse
import com.seravian.core_chat.data.dto.respose.message.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.chat.ChatResponse
import com.seravian.core_chat.data.dto.respose.message.ClientResponse
import com.seravian.core_chat.data.dto.respose.message.ConfirmedMessageResponse
import com.seravian.core_chat.data.dto.respose.chat.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.chat.EditChatResponse
import com.seravian.core_chat.data.dto.respose.chat.IsProcessingResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.ChatDiagnosisResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCheckResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisCreationResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisDetailsResponse
import com.seravian.core_chat.data.dto.respose.diagnosis.DiagnosisReadyResponse
import com.seravian.core_chat.data.dto.respose.message.MessageResponse
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow

class SeravianChatBotDataSource(
    private val authorizedHttpClient: HttpClient,
    private val signalRConnection: RealtimeConnection
): ChatBotRemoteDataSource {
    //////////////////////////////////
    /////////// CHAT METHODS
    /////////////////////////////////

    override suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<CreateChatResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.post(constructUrl("chat/create")) {
                setBody(createChatRequest)
            }
        }
    }

    override suspend fun updateChat(editChatRequest: EditChatRequest): NetworkResult<EditChatResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.put(constructUrl("chat/update")) {
                setBody(editChatRequest)
            }
        }
    }

    override suspend fun deleteChat(deleteChatRequest: DeleteChatRequest): EmptyResult<NetworkError> {
        return safeCall {
            authorizedHttpClient.delete(constructUrl("chat/delete")) {
                url {
                    parameters.append("id", deleteChatRequest.id)
                }
            }
        }
    }

    override suspend fun getChats(): NetworkResult<List<ChatResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/get-chats"))
        }
    }

    override suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<ChatMessagesResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/get-chat-messages")) {
                url {
                    parameters.append("id", getChatMessagesRequest.id)
                }
            }
        }
    }

    override suspend fun syncMessages(
        syncRequest: SyncMessagesRequest
    ): NetworkResult<List<MessageResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/sync-messages")) {
                url {
                    parameters.append(
                        name = "lastMessageId",
                        value = syncRequest.lastMessageId.toString()
                    )
                    parameters.append("chatId", syncRequest.chatId)
                }
            }
        }
    }

    override suspend fun uploadUserVoice(
        uploadVoiceRequest: UploadVoiceRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            authorizedHttpClient.submitFormWithBinaryData(
                url = constructUrl("chat/voice-mode-upload-user-voice"),
                formData = formData {
                    append("chatId", uploadVoiceRequest.chatId)
                    append("voiceFile", uploadVoiceRequest.voiceBytes, Headers.build {
                        append(HttpHeaders.ContentType, "audio/flac")
                        append(HttpHeaders.ContentDisposition, "filename=\"recording.flac\"")
                    })
                }
            )
        }
    }

    override suspend fun isProcessing(
        isProcessingRequest: IsProcessingRequest
    ): NetworkResult<IsProcessingResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/is-processing")) {
                url {
                    parameters.append("chatId", isProcessingRequest.chatId)
                }
            }
        }
    }

    override suspend fun fetchAIAudioResponse(
        fetchAIAudioRequest: FetchAIAudioRequest
    ): NetworkResult<AIAudioResponse, NetworkError> {
        var audioBytes: ByteArray = byteArrayOf()
        var contentType: String = ""
        var fileName: String = ""

        return safeCall<Unit> {
            val response = authorizedHttpClient.get(constructUrl("chat/voice-mode-download-ai-voice")) {
                url {
                    parameters.append(
                        name = "aiAudioId",
                        value = fetchAIAudioRequest.aiAudioId.toString()
                    )
                }
            }

            audioBytes = response.body<ByteArray>()
            contentType = response.headers[HttpHeaders.ContentType] ?: "audio/wav"
            fileName = response.headers[HttpHeaders.ContentDisposition]?.let {
                ContentDisposition.parse(it).parameter("filename")
            } ?: "ai-response.wav"

            response
        }.map {
            AIAudioResponse(
                audioBytes = audioBytes,
                contentType = contentType,
                contentDisposition = fileName
            )
        }
    }

    override suspend fun createDiagnosis(
        creationRequest: DiagnosisCreationRequest
    ): NetworkResult<DiagnosisCreationResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.post(constructUrl("chat/create-chat-diagnosis")) {
                setBody(creationRequest)
            }
        }
    }

    override suspend fun getChatDiagnoses(
        chatDiagnosesRequest: ChatDiagnosesRequest
    ): NetworkResult<List<ChatDiagnosisResponse>, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/get-chat-diagnoses")) {
                url {
                    parameters.append("chatId", chatDiagnosesRequest.chatId)
                }
            }
        }
    }

    override suspend fun getDiagnosisDetails(
        chatDiagnosesRequest: DiagnosisDetailsRequest
    ): NetworkResult<DiagnosisDetailsResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/get-chat-diagnosis-details")) {
                url {
                    parameters.append("chatDiagnosisId", chatDiagnosesRequest.chatDiagnosisId.toString())
                }
            }
        }
    }

    override suspend fun isDiagnosing(
        diagnosingCheckRequest: DiagnosisCheckRequest
    ): NetworkResult<DiagnosisCheckResponse, NetworkError> {
        return safeCall {
            authorizedHttpClient.get(constructUrl("chat/is-diagnosing")) {
                url {
                    parameters.append("chatId", diagnosingCheckRequest.chatId)
                }
            }
        }
    }

    override suspend fun deleteDiagnosis(
        diagnosisDeletionRequest: DiagnosisDeletionRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            authorizedHttpClient.delete(constructUrl("chat/delete-completed-chat-diagnosis")) {
                url {
                    parameters.append("chatDiagnosisId", diagnosisDeletionRequest.chatDiagnosisId.toString())
                }
            }
        }
    }

    override suspend fun deleteDiagnoses(
        diagnosesDeletionRequest: DiagnosesDeletionRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            authorizedHttpClient.delete(constructUrl("chat/delete-completed-chat-diagnoses")) {
                url {
                    parameters.append("chatId", diagnosesDeletionRequest.chatId)
                }
            }
        }
    }

    //////////////////////////////////
    ///////// REALTIME CHAT METHODS
    /////////////////////////////////

    override suspend fun startSignalRConnection() {
        signalRConnection.connect()
        signalRConnection.startCollectingConnectionStatus()
    }

    override suspend fun stopSignalRConnection() {
        signalRConnection.disconnect()
    }

    override fun getSignalRConnectionStatus(): Flow<ConnectionStatus> {
        return signalRConnection.connectionStatus
    }

    override suspend fun joinChat(joinChatRequest: JoinChatRequest) {
        signalRConnection.connection.invoke("join-chat", joinChatRequest)
    }

    override suspend fun sendRequest(clientRequest: ClientRequest) {
        signalRConnection.connection.invoke("send-client-request", clientRequest)
    }

    override fun receiveClientResponse(callback: suspend (ClientResponse) -> Unit) {
        signalRConnection.connection.on(
            target = "receive-client-request",
        ) { response: ClientResponse ->
            callback(response)
        }
    }

    override fun receiveAIResponse(callback: suspend (AIResponse) -> Unit) {
        signalRConnection.connection.on(
            target = "receive-ai-response",
        ) { response: AIResponse ->
            callback(response)
        }
    }

    override fun receiveMessageConfirmation(callback: suspend (ConfirmedMessageResponse) -> Unit) {
        signalRConnection.connection.on(
            target = "confirm-client-request"
        ) { confirmation: ConfirmedMessageResponse ->
            callback(confirmation)
        }
    }

    override suspend fun receiveAIAudioReadyResponse(
        callback: suspend (AIAudioReadyResponse) -> Unit
    ) {
        signalRConnection.connection.on(
            target = "notify-ai-audio-response-ready"
        ) { response: AIAudioReadyResponse ->
            callback(response)
        }
    }

    override fun receiveDiagnosisReadyResponse(
        callback: suspend (DiagnosisReadyResponse) -> Unit
    ) {
        signalRConnection.connection.on(
            target = "notify-chat-diagnosis-ready"
        ) { response: DiagnosisReadyResponse ->
            callback(response)
        }
    }
}