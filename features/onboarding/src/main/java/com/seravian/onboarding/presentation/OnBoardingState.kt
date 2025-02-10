package com.seravian.onboarding.presentation

import androidx.compose.runtime.Immutable
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.networking.data.Result
import com.greenvenom.networking.domain.Error

@Immutable
data class OnBoardingState(
    val currentStep: Int = 1,
    val userDetails: OnBoardingDetails = OnBoardingDetails(),
    val isDetailsFull: Boolean = false,
    val fullNameValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val mobileNumberValidationResult: ValidationResult<String, ValidationError>? = null,
    val uploadingDetailsResult: Result<Any, Error>? = null
)
