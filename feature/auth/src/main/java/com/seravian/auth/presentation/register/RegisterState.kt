package com.seravian.auth.presentation.register

import androidx.compose.runtime.Immutable
import com.seravian.auth.data.AuthError
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result

@Immutable
data class RegisterState(
    val emailValidity: Result<Unit, ValidationError>? = null,
    val usernameValidity: Result<Unit, ValidationError>? = null,
    val passwordValidity: Result<Unit, ValidationError>? = null,
    val confirmPasswordValidity: Result<Unit, ValidationError>? = null,
    val registrationResult: Result<Unit, AuthError>? = null
)