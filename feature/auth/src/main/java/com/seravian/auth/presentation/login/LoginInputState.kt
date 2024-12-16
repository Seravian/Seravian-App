package com.seravian.auth.presentation.login

import androidx.compose.runtime.Immutable
import com.seravian.auth.data.AuthError
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.util.Result

@Immutable
data class LoginInputState(
    val emailValidity: Result<Unit, ValidationError>? = null,
    val passwordValidity: Result<Unit, ValidationError>? = null,
    val loginResult: Result<Unit, AuthError>? = null,
)