package com.greenvenom.core_auth.domain.repository

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

interface AuthRepository {
    suspend fun loginUser(loginRequest: LoginRequest): NetworkResult<LoginResponse, NetworkError>

    suspend fun registerUser(registerRequest: RegisterRequest): NetworkResult<RegisterResponse, NetworkError>

    suspend fun sendOTP(sendOTPRequest: SendOTPRequest): EmptyResult<NetworkError>
    suspend fun updatePassword(newPasswordRequest: NewPasswordRequest): NetworkResult<Any, NetworkError>

    suspend fun verifyOTP(verifyOtpRequest: VerifyOTPRequest): EmptyResult<NetworkError>
}