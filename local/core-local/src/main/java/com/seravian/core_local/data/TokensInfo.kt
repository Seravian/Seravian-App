package com.seravian.core_local.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class TokensInfo(
    val accessToken: String,
    val accessExpiresIn: String,
    val refreshToken: String?
)