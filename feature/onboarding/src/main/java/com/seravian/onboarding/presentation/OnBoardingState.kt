package com.seravian.onboarding.presentation

import androidx.compose.runtime.Immutable
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.seravian.data.errors.AuthError
import com.greenvenom.networking.data.Result

@Immutable
data class OnBoardingState(
    val currentStep: Int = 1,
    val userDetails: OnBoardingDetails = OnBoardingDetails(),
    val isDetailsFull: Boolean = false,
    val fullNameValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val mobileNumberValidationResult: ValidationResult<Unit, ValidationError>? = null,
    val uploadingDetailsResult: Result<Unit, AuthError>? = null
)
