package com.greenvenom.core_network.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class TokenInfo(
    val accessToken: String = "",
    val accessExpiresIn: Int = -1,
    val refreshToken: String = ""
)