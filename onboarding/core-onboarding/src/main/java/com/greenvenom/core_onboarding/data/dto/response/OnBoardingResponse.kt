package com.greenvenom.core_onboarding.data.dto.response

import com.greenvenom.core_network.api.data.dto.response.TokensResponse
import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingResponse(
    val userId: String,
    val email: String,
    val fullName: String?,
    val dateOfBirth: String?,
    val gender: Int?,
    val role: Int?,
    val isEmailVerified: Boolean,
    val isDoctorVerified: Boolean?,
    val isProfileSetupComplete: Boolean,
    val tokens: TokensResponse?
)
