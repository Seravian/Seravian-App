package com.seravian.auth.presentation.user_details

import com.seravian.auth.data.AuthError
import com.seravian.auth.data.AuthUserDetails
import com.seravian.domain.network.Result

data class AuthUserDetailsState(
    val currentStep: Int = 1,
    val userDetails: AuthUserDetails = AuthUserDetails(),
    val isDetailsFull: Boolean = false,
    val uploadingDetailsResult: Result<Unit, AuthError>? = null
)
