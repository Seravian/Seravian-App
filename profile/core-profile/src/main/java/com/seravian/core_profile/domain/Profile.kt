package com.seravian.core_profile.domain

import com.seravian.core_profile.data.local.ProfileEntity
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role

data class Profile(
    val id: String,
    val email: String,
    val fullName: String?,
    val dateOfBirth: String?,
    val gender: Gender?,
    val role: Role?,
    val isEmailVerified: Boolean,
    val isDoctorVerified: Boolean?,
    val isProfileSetupComplete: Boolean,
) {
    fun toProfileEntity() = ProfileEntity(
        id = id,
        email = email,
        fullName = fullName,
        dateOfBirth = dateOfBirth,
        gender = gender,
        role = role,
        isEmailVerified = isEmailVerified,
        isDoctorVerified = isDoctorVerified,
        isProfileSetupComplete = isProfileSetupComplete,
    )
}