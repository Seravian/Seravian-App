package com.seravian.auth.presentation.user_details

import com.seravian.auth.data.AuthError
import com.seravian.auth.data.AuthUserDetails
import com.seravian.auth.util.ValidationError
import com.seravian.domain.network.Result

data class AuthUserDetailsState(
    val currentStep: Int = 1,
    val userDetails: AuthUserDetails = AuthUserDetails(),
    val isDetailsFull: Boolean = false,
    val fullNameValidationResult: Result<Unit, ValidationError>? = null,
    val mobileNumberValidationResult: Result<Unit, ValidationError>? = null,
    val uploadingDetailsResult: Result<Unit, AuthError>? = null
)
