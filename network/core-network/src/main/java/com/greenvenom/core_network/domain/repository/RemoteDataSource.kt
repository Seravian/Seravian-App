package com.greenvenom.core_network.domain.repository

import com.greenvenom.core_auth.data.dto.request.SendOTPRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.VerifyOTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_onboarding.data.dto.request.OnBoardingRequest
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
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
import com.seravian.core_chat.data.dto.respose.CreateChatResponse
import com.seravian.core_chat.data.dto.respose.EditChatResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>
    suspend fun verifyOtp(verifyOtpRequest: VerifyOTPRequest): EmptyResult<NetworkError>
    suspend fun sendOtp(sendOTPRequest: SendOTPRequest): EmptyResult<NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>
    suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError>
    suspend fun logoutUser(refreshToken: String): EmptyResult<NetworkError>
    suspend fun refreshTokens(refreshTokenRequest: RefreshTokenRequest): NetworkResult<TokensResponse, NetworkError>
    suspend fun createChat(createChatRequest: CreateChatRequest): NetworkResult<CreateChatResponse,NetworkError>
    suspend fun updateChat(editChatRequest: EditChatRequest):NetworkResult<EditChatResponse,NetworkError>
    suspend fun deleteChat(deleteChatRequest: DeleteChatRequest):EmptyResult<NetworkError>
    suspend fun getChats(): NetworkResult<List<ChatResponse>, NetworkError>
    suspend fun getChatMessages(
        getChatMessagesRequest: GetChatMessagesRequest
    ): NetworkResult<ChatMessagesResponse, NetworkError>
    suspend fun startSignalRConnection()
    suspend fun stopSignalRConnection()
    fun joinChat(joinChatRequest: JoinChatRequest)
    fun sendRequest(clientRequest: ClientRequest)
    fun receiveClientRequest(): Flow<ClientResponse>
    fun receiveAIResponse(): Flow<AIResponse>
}