package com.seravian.auth.presentation.reset_password

import androidx.compose.runtime.Immutable
import com.seravian.data.errors.AuthError
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.networking.data.Result

@Immutable
data class ResetPasswordState(
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val confirmPasswordValidity: ValidationResult<Unit, ValidationError>? = null,
    val resetPasswordResult: Result<Unit, AuthError>? = null,
)