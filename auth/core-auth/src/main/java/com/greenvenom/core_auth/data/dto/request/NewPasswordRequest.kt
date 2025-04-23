package com.greenvenom.core_auth.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordRequest(
    val email: String,
    val newPassword: String
)
