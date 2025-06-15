package com.seravian.core_profile.domain

import com.seravian.core_profile.data.local.ProfileEntity
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role
import com.seravian.core_verification.domain.utils.DoctorTitle

data class Profile(
    val id: String,
    val email: String,
    val fullName: String?,
    val dateOfBirth: String?,
    val createdAtUtc: String,
    val gender: Gender?,
    val role: Role?,
    val isEmailVerified: Boolean,
    val isProfileSetupComplete: Boolean,
    val doctorTitle: DoctorTitle? = null,
    val doctorDescription: String? = null,
    val doctorSessionPrice: Int? = null,
    val doctorVerifiedAtUtc: String? = null,
    val profileImageUrl: String? = null
) {
    fun toProfileEntity() = ProfileEntity(
        id = id,
        email = email,
        fullName = fullName,
        dateOfBirth = dateOfBirth,
        createdAtUtc = createdAtUtc,
        gender = gender,
        role = role,
        isEmailVerified = isEmailVerified,
        isProfileSetupComplete = isProfileSetupComplete,
        doctorTitle = doctorTitle,
        doctorDescription = doctorDescription,
        doctorSessionPrice = doctorSessionPrice,
        doctorVerifiedAtUtc = doctorVerifiedAtUtc,
        profileImageUrl = profileImageUrl
    )
}