package com.greenvenom.core_auth.data.dto.response

import com.greenvenom.core_tokens.data.dto.response.TokensResponse
import com.seravian.core_profile.domain.Profile
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
) {
    fun extractProfile(): Profile {
        return Profile(
            id = this.userId,
            email = this.email,
            fullName = this.fullName,
            dateOfBirth = this.dateOfBirth,
            role = this.role,
            gender = this.gender,
            isEmailVerified = this.isEmailVerified,
            isDoctorVerified = this.isDoctorVerified,
            isProfileSetupComplete = this.isProfileSetupComplete
        )
    }
}
