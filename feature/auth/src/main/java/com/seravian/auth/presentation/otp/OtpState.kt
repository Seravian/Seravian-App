package com.seravian.auth.presentation.otp

import com.seravian.auth.data.AuthError
import com.seravian.domain.network.Result

data class OtpState(
    val code: List<Int?> = (1..4).map { null },
    val email: String? = null,
    val focusedIndex: Int? = null,
    val otpResult: Result<Unit, AuthError>? = null
)