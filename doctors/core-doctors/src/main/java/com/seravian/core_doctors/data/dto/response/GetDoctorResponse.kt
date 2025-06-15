package com.seravian.core_doctors.data.dto.response

import com.seravian.core_doctors.domain.models.Doctor
import com.seravian.core_profile.domain.utils.Gender
import com.seravian.core_verification.domain.utils.DoctorTitle
import kotlinx.serialization.Serializable

@Serializable
data class GetDoctorResponse(
    val doctorId: String,
    val doctorTitle: Int,
    val doctorDescription: String,
    val doctorSessionPrice: Int,
    val doctorFullName: String,
    val doctorAge: Int,
    val doctorGender: Int,
    val doctorImageUrl: String?

) {
    fun extractDoctor(): Doctor {
        return Doctor(
            doctorId = doctorId,
            doctorTitle = DoctorTitle.entries[doctorTitle],
            doctorDescription = doctorDescription,
            doctorSessionPrice = doctorSessionPrice,
            doctorFullName = doctorFullName,
            doctorAge = doctorAge,
            doctorGender = Gender.entries[doctorGender],
            doctorImageUrl = doctorImageUrl
        )
    }
}
