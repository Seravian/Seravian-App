package com.seravian.feat_network.domain

import com.greenvenom.core_auth.data.dto.request.ResetPasswordRequest
import com.greenvenom.core_auth.data.dto.request.LoginRequest
import com.greenvenom.core_auth.data.dto.request.NewPasswordRequest
import com.greenvenom.core_auth.data.dto.request.OTPRequest
import com.greenvenom.core_auth.data.dto.request.RegisterRequest
import com.greenvenom.core_auth.data.dto.response.LoginResponse
import com.greenvenom.core_auth.data.dto.response.RegisterResponse
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_local.data.TokensInfo

interface RemoteDataSource {
    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>
    suspend fun verifyOtp(otpRequest: OTPRequest): EmptyResult<NetworkError>
    suspend fun sendResetPasswordEmail(resetPasswordRequest: ResetPasswordRequest): NetworkResult<Any, NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): NetworkResult<Any, NetworkError>
    suspend fun logoutUser(): NetworkResult<Any, NetworkError>
    suspend fun refreshToken(refreshToken: String): NetworkResult<TokensInfo, NetworkError>
}