package com.greenvenom.feat_onboarding.presentation

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.greenvenom.core_onboarding.data.dto.response.OnBoardingResponse
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

@Immutable
data class OnBoardingState(
    val currentStep: Int = 1,
    val userDetails: OnBoardingDetails = OnBoardingDetails(),
    val isDataFull: Boolean = false,
    val fullNameValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val uploadingDetailsResult: NetworkResult<OnBoardingResponse, NetworkError>? = null
)
