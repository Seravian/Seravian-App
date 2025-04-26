package com.greenvenom.core_tokens.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
