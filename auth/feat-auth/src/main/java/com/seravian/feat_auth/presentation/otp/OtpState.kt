package com.seravian.feat_auth.presentation.otp

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError

@Immutable
data class OtpState(
    val code: List<String?> = (1..8).map { null },
    val focusedIndex: Int? = null,
    val otpNetworkResult: EmptyResult<NetworkError>? = null
)