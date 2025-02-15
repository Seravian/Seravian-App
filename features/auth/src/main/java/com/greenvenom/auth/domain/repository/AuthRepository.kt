package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Result<Any, Error>

    suspend fun registerUser(email: String, password: String, displayName: String): Result<Any?, Error>
    suspend fun verifyUserRegistration(email: String, otp: String): Result<Any, Error>

    suspend fun sendResetPasswordEmail(email: String): Result<Any, Error>
    suspend fun updatePassword(newPassword: String): Result<Any, Error>

    suspend fun verifyOtp(email: String, otp: String): Result<Any, Error>
}