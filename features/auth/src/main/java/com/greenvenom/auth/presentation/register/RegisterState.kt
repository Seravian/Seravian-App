package com.greenvenom.auth.presentation.register

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

@Immutable
data class RegisterState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val usernameValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val confirmPasswordValidity: ValidationResult<Unit, ValidationError>? = null,
    val registrationNetworkResult: NetworkResult<Any?, NetworkError>? = null
)