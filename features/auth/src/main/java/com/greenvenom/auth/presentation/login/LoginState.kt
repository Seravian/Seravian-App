package com.greenvenom.auth.presentation.login

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

@Immutable
data class LoginState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val loginNetworkResult: NetworkResult<Any, NetworkError>? = null,
)