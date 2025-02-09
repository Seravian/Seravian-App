package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError

interface RegisterRepository {
    suspend fun registerUser(email: String, password: String, displayName: String): Result<Any?, NetworkError>
    suspend fun verifyUserRegistration(email: String, otp: String): Result<Any, NetworkError>
}