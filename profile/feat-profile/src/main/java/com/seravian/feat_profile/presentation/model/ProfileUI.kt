package com.seravian.feat_profile.presentation.model

import com.seravian.core_profile.domain.Profile

data class ProfileUI(
    val email: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val role: String = "",
    val isEmailVerified: Boolean = true,
    val isDoctorVerified: Boolean ?= true
)

fun Profile.toProfileUI() = ProfileUI(
    email = email,
    fullName = fullName ?: "",
    dateOfBirth = dateOfBirth ?: "",
    gender = gender?.value ?: "",
    role = role?.value ?: "",
    isEmailVerified = isEmailVerified,
    isDoctorVerified = isDoctorVerified
)