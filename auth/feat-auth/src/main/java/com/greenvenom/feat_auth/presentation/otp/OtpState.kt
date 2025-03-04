package com.greenvenom.feat_auth.presentation.otp

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult

@Immutable
data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
    val otpNetworkResult: NetworkResult<Any, NetworkError>? = null
)