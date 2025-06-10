package com.seravian.core_profile.data.remote.response

import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_verification.domain.utils.DoctorTitle
import kotlinx.serialization.Serializable

@Serializable
data class DoctorProfileResponse(
    val id: String,
    val email: String,
    val fullName: String?,
    val title: DoctorTitle?,
    val doctorDescription: String? = null,
    val dateOfBirth: String?,
    val gender: Gender?,
    val createdAtUtc: String,
    val verifiedAtUtc: String? = null,
)