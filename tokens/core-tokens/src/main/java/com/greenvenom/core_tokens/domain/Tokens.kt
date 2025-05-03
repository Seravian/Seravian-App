package com.greenvenom.core_tokens.domain

import com.greenvenom.core_tokens.data.dto.request.RefreshTokenRequest
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.Instant

@Serializable
data class Tokens(
    val accessToken: String = "",
    val accessTokenExpirationUtc: String = "",
    val refreshToken: String ?= null
) {
    fun toRefreshTokenRequest(): RefreshTokenRequest {
        return RefreshTokenRequest(refreshToken = refreshToken ?: "")
    }

    fun isAccessExpired(): Boolean {
        val expirationInstant = Instant.parse(this.accessTokenExpirationUtc)
        val currentInstant = Instant.now()

        return expirationInstant.minus(Duration.ofMinutes(2)).isBefore(currentInstant)
    }
}