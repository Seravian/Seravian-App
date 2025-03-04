package com.greenvenom.core_network.data

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.domain.Error

@Immutable
data class NetworkError(
    val errorType: ErrorType? = null
): Error
