package com.seravian.core_network.data

import androidx.compose.runtime.Immutable
import com.seravian.core_network.domain.Error

@Immutable
data class NetworkError(
    val errorType: ErrorType? = null
): Error
