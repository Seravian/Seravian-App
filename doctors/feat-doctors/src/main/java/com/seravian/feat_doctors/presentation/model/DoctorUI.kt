package com.seravian.feat_doctors.presentation.model

import com.seravian.core_doctors.domain.models.Doctor
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_verification.domain.utils.DoctorTitle

data class DoctorUI(
    val profileImageUrl: String?,
    val name: String,
    val salary: String,
    val description: String,
    val title : String,
    val gender: String
)

fun Doctor.toDoctorUI():DoctorUI{
    return DoctorUI(
        name = doctorFullName,
        title = doctorTitle.title,
        salary = doctorSessionPrice.toString(),
        description = doctorDescription,
        gender = doctorGender.value,
        profileImageUrl = doctorImageUrl
    )
}


