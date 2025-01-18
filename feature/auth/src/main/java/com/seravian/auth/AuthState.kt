package com.seravian.auth

import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

data class AuthState(
    val email: String? = null,
    val emailValidity: ValidationResult<Unit, ValidationError>? = null
)