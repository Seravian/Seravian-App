package com.greenvenom.core_tokens.domain

import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    val accessToken: String,
    val accessExpiresIn: String,
    val refreshToken: String?
) {
    fun toRefreshTokenRequest(): RefreshTokenRequest {
        return RefreshTokenRequest(refreshToken = refreshToken ?: "")
    }
}