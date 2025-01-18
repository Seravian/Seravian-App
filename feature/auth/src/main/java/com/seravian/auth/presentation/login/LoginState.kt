package com.seravian.auth.presentation.login

import androidx.compose.runtime.Immutable
import com.seravian.data.errors.AuthError
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.networking.data.Result

@Immutable
data class LoginState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val loginResult: Result<Unit, AuthError>? = null,
)