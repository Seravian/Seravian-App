package com.seravian.auth.presentation.register

import androidx.compose.runtime.Immutable
import com.seravian.data.errors.AuthError
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.networking.data.Result

@Immutable
data class RegisterState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val usernameValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val confirmPasswordValidity: ValidationResult<Unit, ValidationError>? = null,
    val registrationResult: Result<Unit, AuthError>? = null
)