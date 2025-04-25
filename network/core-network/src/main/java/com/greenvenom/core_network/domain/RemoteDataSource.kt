package com.greenvenom.core_network.domain

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
import com.greenvenom.core_tokens.domain.Tokens

interface RemoteDataSource {
    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>
    suspend fun verifyOtp(verifyOtpRequest: VerifyOTPRequest): EmptyResult<NetworkError>
    suspend fun sendOtp(sendOTPRequest: SendOTPRequest): EmptyResult<NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>
    suspend fun updateUserDetails(
        onBoardingRequest: OnBoardingRequest
    ): NetworkResult<OnBoardingResponse, NetworkError>
    suspend fun refreshToken(
        refreshTokenRequest: RefreshTokenRequest
    ): NetworkResult<TokensResponse, NetworkError>
    suspend fun logoutUser(refreshToken: String): EmptyResult<NetworkError>
}