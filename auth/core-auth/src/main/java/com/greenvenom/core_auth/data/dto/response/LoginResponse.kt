package com.greenvenom.core_auth.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
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
