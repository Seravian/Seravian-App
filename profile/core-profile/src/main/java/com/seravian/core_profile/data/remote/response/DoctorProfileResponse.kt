package com.seravian.core_profile.data.remote.response

import com.seravian.core_profile.domain.Profile
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_profile.domain.utils.Role
import com.seravian.core_verification.domain.utils.DoctorTitle
import kotlinx.serialization.Serializable

@Serializable
data class DoctorProfileResponse(
    val id: String,
    val email: String,
    val fullName: String?,
    val title: DoctorTitle?,
    val doctorDescription: String? = null,
    val doctorSessionPrice: Int? = null,
    val dateOfBirth: String?,
    val gender: Gender?,
    val createdAtUtc: String,
    val verifiedAtUtc: String? = null,
    val profileImageUrl: String? = null
) {
    fun extractProfile(): Profile {
        return Profile(
            id = id,
            email = email,
            fullName = fullName,
            dateOfBirth = dateOfBirth,
            createdAtUtc = createdAtUtc,
            gender = gender,
            role = Role.DOCTOR,
            isEmailVerified = true,
            isProfileSetupComplete = true,
            doctorTitle = title,
            doctorDescription = doctorDescription,
            doctorSessionPrice = doctorSessionPrice,
            doctorVerifiedAtUtc = verifiedAtUtc,
            profileImageUrl = profileImageUrl
        )
    }
}