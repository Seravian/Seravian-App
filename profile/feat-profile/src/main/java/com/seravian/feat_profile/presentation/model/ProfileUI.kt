package com.seravian.feat_profile.presentation.model

import com.greenvenom.core_ui.utils.formatDateTime
import com.seravian.core_profile.domain.Profile
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role
import com.seravian.core_verification.domain.utils.DoctorTitle

data class ProfileUI(
    val id: String,
    val fullName: String,
    val email: String,
    val profileImageUrl: String?,
    val accountSince: String,
    val role: Role,
    val gender: Gender?,
    val dateOfBirth: String?,

    // Doctor-specific
    val doctorTitle: DoctorTitle?,
    val doctorDescription: String?,
    val doctorSessionPrice: String?,
    val doctorVerifiedAt: String?
)

fun Profile.toProfileUI(): ProfileUI {
    return ProfileUI(
        id = id,
        fullName = fullName.orEmpty(),
        email = email,
        profileImageUrl = profileImageUrl,
        accountSince = formatDateTime(createdAtUtc, false) ?: "",
        role = role ?: Role.PATIENT,
        gender = gender,
        dateOfBirth = dateOfBirth?.let { formatDateTime(it) },
        doctorTitle = doctorTitle,
        doctorDescription = doctorDescription,
        doctorSessionPrice = doctorSessionPrice?.let { "${it / 100.0} EGP" },
        doctorVerifiedAt = doctorVerifiedAtUtc?.let { formatDateTime(it) }
    )
}