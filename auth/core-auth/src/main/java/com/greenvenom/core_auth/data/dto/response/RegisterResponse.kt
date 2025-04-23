package com.greenvenom.core_auth.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val id: String,
    val email: String,
    val createdAtUtc: String
)
