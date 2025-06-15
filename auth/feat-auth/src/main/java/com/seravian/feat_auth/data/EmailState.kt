package com.seravian.feat_auth.data

import com.seravian.validation.domain.ValidationError
import com.seravian.validation.domain.ValidationResult

data class EmailState(
    val email: String? = null,
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
)