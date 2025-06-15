package com.seravian.feat_auth.presentation.register

import androidx.compose.runtime.Immutable
import com.seravian.core_auth.data.dto.response.RegisterResponse
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.validation.domain.ValidationError
import com.seravian.validation.domain.ValidationResult

@Immutable
data class RegisterState(
    val emailValidity: ValidationResult<Unit, ValidationError>? = null,
    val passwordValidity: ValidationResult<Unit, ValidationError>? = null,
    val confirmPasswordValidity: ValidationResult<Unit, ValidationError>? = null,
    val registrationNetworkResult: NetworkResult<RegisterResponse, NetworkError>? = null
)