package com.greenvenom.core_network.api.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class TokensResponse(
    val accessToken: String,
    val accessTokenExpirationUtc: String,
    val refreshToken: String?,
)