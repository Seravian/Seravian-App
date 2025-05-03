package com.seravian.feat_network.data

import android.util.Log
import com.greenvenom.core_auth.data.dto.request.SendOTPRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.VerifyOTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_network.api.utils.constructUrl
import com.greenvenom.core_network.api.utils.safeCall
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.domain.ConnectionStatus
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.core_network.domain.repository.RemoteDataSource
import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.seravian.core_chat.data.dto.request.ClientRequest
import com.seravian.core_chat.data.dto.request.CreateChatRequest
import com.seravian.core_chat.data.dto.request.DeleteChatRequest
import com.seravian.core_chat.data.dto.request.EditChatRequest
import com.seravian.core_chat.data.dto.request.GetChatMessagesRequest
import com.seravian.core_chat.data.dto.request.JoinChatRequest
import com.seravian.core_chat.data.dto.respose.AIResponse
import com.seravian.core_chat.data.dto.respose.ChatMessagesResponse
import com.seravian.core_chat.data.dto.respose.ChatResponse
import com.seravian.core_chat.data.dto.respose.ClientResponse
import com.seravian.core_chat.data.dto.respose.ConfirmedMessageResponse
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import com.seravian.core_profile.data.remote.request.LogoutRequest
import eu.lepicekmichal.signalrkore.HubConnection
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SeravianDataSource(
    private val publicHttpClient: HttpClient,
    private val authorizedHttpClient: HttpClient,
    private val signalRConnection: SignalRConnection
): RemoteDataSource {

    /////////////////////////////////
    /////////// AUTH METHODS
    /////////////////////////////////

    override suspend fun registerUser(
        registerRequest: RegisterRequest
    ): NetworkResult<RegisterResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/register")) {
                setBody(registerRequest)
            }
        }
    }

    override suspend fun loginUser(
        loginRequest: LoginRequest
    ): NetworkResult<LoginResponse, NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/login")) {
                setBody(loginRequest)
            }
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/verify-otp")) {
                setBody(verifyOtpRequest)
            }
        }
    }

    override suspend fun sendOtp(
        sendOTPRequest: SendOTPRequest
    ): EmptyResult<NetworkError> {
        return safeCall {
            publicHttpClient.post(constructUrl("auth/resend-otp")) {
                setBody(
                    mapOf(
                        "email" to sendOTPRequest.email
                    )
                )
            }
        }
    }

    override suspend fun updatePassword(
        newPasswordRequest: NewPasswordRequest
    ): NetworkResult<Any, NetworkError> {
        Log.d("SeravianDS", "Updating Password")
        return NetworkResult.Success(Unit)
    }

    override suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError> {
        return safeCall<OnBoardingResponse> {
            authorizedHttpClient.post(constructUrl("auth/complete-profile-setup")) {
                setBody(onBoardingRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }

    override suspend fun logoutUser(logoutRequest: LogoutRequest): EmptyResult<NetworkError> {
        return safeCall<Unit> {
            authorizedHttpClient.post(constructUrl("auth/logout")) {
                setBody(logoutRequest)
            }
        }.onSuccess { authorizedHttpClient.authProvider<BearerAuthProvider>()?.clearToken() }
    }

    override suspend fun refreshTokens(refreshTokenRequest: RefreshTokenRequest): NetworkResult<TokensResponse, NetworkError> {
        return safeCall<TokensResponse> {
            publicHttpClient.post(constructUrl("auth/refresh-token")) {
                setBody(refreshTokenRequest)
            }
        }
    }

    /////////////////////////////////
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

    /////////////////////////////////
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
        signalRConnection.hubConnection.invoke("join-chat", joinChatRequest)
    }

    override suspend fun sendRequest(clientRequest: ClientRequest) {
        signalRConnection.hubConnection.invoke("send-client-request", clientRequest)
    }

    override fun receiveClientResponse(callback: (ClientResponse) -> Unit) {
        signalRConnection.hubConnection.on(
            "receive-client-request",
        ) { response: ClientResponse ->
            callback(response)
        }
    }

    override fun receiveAIResponse(callback: (AIResponse) -> Unit) {
        signalRConnection.hubConnection.on(
            "receive-ai-response",
        ) { response: AIResponse ->
            callback(response)
        }
    }

    override fun receiveMessageConfirmation(callback: (ConfirmedMessageResponse) -> Unit) {
        signalRConnection.hubConnection.on(
            target = "confirm-client-request"
        ) { confirmation: ConfirmedMessageResponse ->
            callback(confirmation)
        }
    }
}