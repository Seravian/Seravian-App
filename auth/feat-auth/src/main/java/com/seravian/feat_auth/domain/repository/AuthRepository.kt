package com.seravian.feat_auth.domain.repository

import com.seravian.core_auth.data.dto.request.SendOTPRequest
import com.seravian.core_auth.data.dto.request.LoginRequest
import com.seravian.core_auth.data.dto.request.NewPasswordRequest
import com.seravian.core_auth.data.dto.request.VerifyOTPRequest
import com.seravian.core_auth.data.dto.request.RegisterRequest
import com.seravian.core_auth.data.dto.response.LoginResponse
import com.seravian.core_auth.data.dto.response.RegisterResponse
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult

interface AuthRepository {
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>

    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>

    suspend fun sendOTP(sendOTPRequest: SendOTPRequest): EmptyResult<NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>

    suspend fun verifyOTP(verifyOtpRequest: VerifyOTPRequest): EmptyResult<NetworkError>
}