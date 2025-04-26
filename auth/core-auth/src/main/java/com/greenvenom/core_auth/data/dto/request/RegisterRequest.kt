package com.greenvenom.core_auth.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String
)
