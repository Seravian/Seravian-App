package com.seravian.auth.domain.repository

interface OtpRepository {
    suspend fun verifyOtp(email: String, otp: String)
}