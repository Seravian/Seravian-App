package com.greenvenom.core_auth.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class OTPRequest(
    val email: String,
    val otp: String
)
