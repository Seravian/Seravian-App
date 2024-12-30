package com.seravian.auth

import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result

data class AuthState(
    val email: String? = null,
    val emailValidity: Result<Unit, ValidationError>? = null
)