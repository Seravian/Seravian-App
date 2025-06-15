package com.seravian.feat_onboarding.presentation

import androidx.compose.runtime.Immutable
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_onboarding.data.dto.response.OnBoardingResponse
import com.seravian.validation.domain.ValidationError
import com.seravian.validation.domain.ValidationResult

@Immutable
data class OnBoardingState(
    val currentStep: Int = 1,
    val userDetails: OnBoardingDetails = OnBoardingDetails(),
    val isDataFull: Boolean = false,
    val fullNameValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val uploadingDetailsResult: NetworkResult<OnBoardingResponse, NetworkError>? = null
)
