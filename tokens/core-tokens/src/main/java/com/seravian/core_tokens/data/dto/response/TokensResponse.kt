package com.seravian.core_tokens.data.dto.response

import com.seravian.core_tokens.domain.Tokens
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
            accessTokenExpirationUtc = this.accessTokenExpirationUtc.substringBeforeLast(".") + "Z",
            refreshToken = this.refreshToken
        )
    }
}