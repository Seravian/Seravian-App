package com.greenvenom.auth.presentation.reset_password

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.NetworkError
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

@Immutable
data class ResetPasswordState(
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val confirmPasswordValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordUpdatedResult: Result<Any, NetworkError>? = null,
    val emailSentResult: Result<Any, NetworkError>? = null
)