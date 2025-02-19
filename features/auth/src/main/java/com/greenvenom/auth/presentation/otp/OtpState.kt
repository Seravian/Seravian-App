package com.greenvenom.auth.presentation.otp

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult

@Immutable
data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
    val otpNetworkResult: NetworkResult<Any, NetworkError>? = null
)