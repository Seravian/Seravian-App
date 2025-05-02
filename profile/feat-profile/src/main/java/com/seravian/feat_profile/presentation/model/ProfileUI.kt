package com.seravian.feat_profile.presentation.model

import com.seravian.core_profile.domain.Profile

data class ProfileUI(
    val id: String = "",
    val email: String = "",
    val fullName: String? = "",
    val dateOfBirth: String? = "",
    val gender: Int? = 0,
    val role: Int? = 0,
    val isEmailVerified: Boolean = true,
    val isDoctorVerified: Boolean?  = true,
    val isProfileSetupComplete: Boolean = true,
)

fun Profile.toProfileUI() = ProfileUI(
    id = id,
    email = email,
    fullName = fullName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    role = role,
    isEmailVerified = isEmailVerified,
    isDoctorVerified = isDoctorVerified,
    isProfileSetupComplete = isProfileSetupComplete
)