package com.seravian.auth.presentation.reset_password

import com.seravian.auth.data.AuthError
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result

data class ResetPasswordState(
    val passwordValidity: Result<Unit, ValidationError>? = null,
    val confirmPasswordValidity: Result<Unit, ValidationError>? = null,
    val resetPasswordResult: Result<Unit, AuthError>? = null,
)