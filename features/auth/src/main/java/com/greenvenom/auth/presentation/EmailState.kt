package com.greenvenom.auth.presentation

import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

data class EmailState(
    val email: String? = null,
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
)