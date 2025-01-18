package com.seravian.auth.presentation.otp

import androidx.compose.runtime.Immutable
import com.seravian.data.AuthError
import com.seravian.domain.network.Result

@Immutable
data class OtpState(
    val code: List<Int?> = (1..4).map { null },
    val focusedIndex: Int? = null,
    val otpResult: Result<Unit, AuthError>? = null
)