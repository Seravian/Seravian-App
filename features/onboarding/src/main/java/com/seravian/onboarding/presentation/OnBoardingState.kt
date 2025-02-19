package com.seravian.onboarding.presentation

import androidx.compose.runtime.Immutable
import com.greenvenom.networking.data.NetworkError
import com.greenvenom.networking.data.NetworkResult
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult

@Immutable
data class OnBoardingState(
    val currentStep: Int = 1,
    val userDetails: OnBoardingDetails = OnBoardingDetails(),
    val isDetailsFull: Boolean = false,
    val fullNameValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val mobileNumberValidationResult: ValidationResult<String, ValidationError>? = null,
    val uploadingDetailsResult: NetworkResult<Any, NetworkError>? = null
)
