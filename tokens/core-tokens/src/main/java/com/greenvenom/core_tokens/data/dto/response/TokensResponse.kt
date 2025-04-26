package com.greenvenom.core_tokens.data.dto.response

import com.greenvenom.core_tokens.domain.Tokens
import kotlinx.serialization.Serializable

@Serializable
data class TokensResponse(
    val accessToken: String,
    val accessTokenExpirationUtc: String,
    val refreshToken: String?,
) {
    fun extractTokens(): Tokens {
        return Tokens(
            accessToken = this.accessToken,
            accessExpiresIn = this.accessTokenExpirationUtc,
            refreshToken = this.refreshToken
        )
    }
}