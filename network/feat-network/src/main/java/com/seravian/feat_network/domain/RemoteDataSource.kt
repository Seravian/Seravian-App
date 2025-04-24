package com.seravian.feat_network.domain

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
import com.seravian.core_local.data.TokensInfo

interface RemoteDataSource {
    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>
    suspend fun verifyOtp(verifyOtpRequest: VerifyOTPRequest): EmptyResult<NetworkError>
    suspend fun sendOtp(sendOTPRequest: SendOTPRequest): EmptyResult<NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): NetworkResult<Any, NetworkError>
    suspend fun logoutUser(refreshToken: String): EmptyResult<NetworkError>
}