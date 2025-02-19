package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): NetworkResult<Any, NetworkError>

    suspend fun registerUser(email: String, password: String, displayName: String): NetworkResult<Any?, NetworkError>
    suspend fun verifyUserRegistration(email: String, otp: String): NetworkResult<Any, NetworkError>

    suspend fun sendResetPasswordEmail(email: String): NetworkResult<Any, NetworkError>
    suspend fun updatePassword(newPassword: String): NetworkResult<Any, NetworkError>

    suspend fun verifyOtp(email: String, otp: String): NetworkResult<Any, NetworkError>
}