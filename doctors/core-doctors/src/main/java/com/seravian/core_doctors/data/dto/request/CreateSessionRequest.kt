package com.seravian.core_doctors.data.dto.request

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CreateSessionRequest(
    val doctorId:String,
    val patientIsAvailableFromUtc: String,
    val patientIsAvailableToUtc: String,
    val patientNote: String?
)