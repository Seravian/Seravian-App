package com.seravian.core_doctors.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class GetDoctorRequest(
    val id: String
)