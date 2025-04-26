package com.greenvenom.core_auth.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOTPRequest(
    val email: String,
    val otpCode: String
)
