package com.greenvenom.networking.domain.datasource

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError

interface RemoteDataSource {
    suspend fun registerUser(displayName: String, email: String, password: String): Result<Any?, NetworkError>
    suspend fun loginUser(email: String, password: String): Result<Any, NetworkError>
    suspend fun verifyOtp(email: String, otp: String): Result<Any, NetworkError>
    suspend fun sendResetPasswordEmail(email: String): Result<Any, NetworkError>
    suspend fun updatePassword(password: String): Result<Any, NetworkError>
    suspend fun updateUserDetails(
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): Result<Any, NetworkError>
    suspend fun logoutUser(): Result<Any, NetworkError>
}