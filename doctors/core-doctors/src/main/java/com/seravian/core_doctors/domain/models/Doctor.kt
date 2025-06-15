package com.seravian.core_doctors.domain.models

import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_verification.domain.utils.DoctorTitle

data class Doctor(
    val doctorId: String,
    val doctorTitle: DoctorTitle,
    val doctorDescription: String,
    val doctorSessionPrice: Int,
    val doctorFullName: String,
    val doctorAge: Int,
    val doctorGender: Gender,
    val doctorImageUrl: String?
)
