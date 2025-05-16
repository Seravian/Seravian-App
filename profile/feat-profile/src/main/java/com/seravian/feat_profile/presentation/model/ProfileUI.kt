package com.seravian.feat_profile.presentation.model

import com.seravian.core_profile.domain.Profile
import com.seravian.feat_profile.data.Gender
import com.seravian.feat_profile.data.Role

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
    gender = Gender.entries[gender ?: 0].value,
    role = Role.entries[role ?: 0].value,
    isEmailVerified = isEmailVerified,
    isDoctorVerified = isDoctorVerified
)