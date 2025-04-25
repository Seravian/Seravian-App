package com.greenvenom.core_onboarding.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class OnBoardingRequest(
    val fullName: String,
    val dateOfBirth: String,
    val gender: Int,
    val role: Int
)