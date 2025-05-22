package com.seravian.feat_chat.data

import android.util.Log
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.ConnectionStatus
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.map
import com.greenvenom.core_network.domain.RealtimeConnection
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.request.SyncMessagesRequest
import com.seravian.core_chat.data.dto.request.UploadVoiceRequest
import com.seravian.core_chat.data.dto.respose.AIAudioReadyResponse
import com.seravian.core_chat.data.dto.respose.AIAudioResponse
import com.seravian.core_chat.data.dto.respose.AIResponse
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.ClientResponse
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_chat.data.dto.respose.MessageResponse
import com.seravian.feat_chat.domain.ChatRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class SeravianChatDataSource(
    private val authorizedHttpClient: HttpClient,
    private val signalRConnection: RealtimeConnection
): ChatRemoteDataSource {
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
                setBody(deleteChatRequest)
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
                        name = "lastMessageTimestampUtc",
                        value = syncRequest.lastMessageTimestampUtc
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
            ) {
                timeout {
                    requestTimeoutMillis = 60_000L
                    connectTimeoutMillis = 15_000L
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
                fileName = fileName
            )
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
            Log.d("receiveAIAudioReadyResponse", response.toString())
            callback(response)
        }
    }
}