package com.greenvenom.networking.data

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.domain.Error

@Immutable
data class NetworkError(
    val errorType: ErrorType? = null
): Error
