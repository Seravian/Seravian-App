package com.seravian.domain.datasource

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error

interface RemoteDataSource {
    suspend fun registerUser(username: String, email: String, password: String): Result<Any, Error>
    suspend fun loginUser(email: String, password: String): Result<Any, Error>
    suspend fun verifyOtp(email: String, otp: String): Result<Any, Error>
    suspend fun sendResetPasswordEmail(email: String): Result<Any, Error>
    suspend fun updatePassword(password: String): Result<Any, Error>
    suspend fun updateUserDetails(
        fullName: String,
        userType: String,
        phoneNumber: String,
        birthDate: String,
        gender: String
    ): Result<Any, Error>
    suspend fun logoutUser(): Result<Any, Error>
}