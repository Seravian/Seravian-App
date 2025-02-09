package com.greenvenom.auth.domain.repository

import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError

interface OtpRepository {
    suspend fun verifyOtp(email: String, otp: String): Result<Any, NetworkError>
}