package com.greenvenom.core_auth.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TokensResponse(
    val accessToken: String,
    val accessTokenExpirationUtc: String,
    val refreshToken: String?,
)