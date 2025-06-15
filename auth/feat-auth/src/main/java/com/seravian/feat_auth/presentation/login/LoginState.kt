package com.seravian.feat_auth.presentation.login

import androidx.compose.runtime.Immutable
import com.seravian.core_auth.data.dto.response.LoginResponse
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.validation.domain.ValidationError
import com.seravian.validation.domain.ValidationResult

@Immutable
data class LoginState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val loginNetworkResult: NetworkResult<LoginResponse, NetworkError>? = null,
)