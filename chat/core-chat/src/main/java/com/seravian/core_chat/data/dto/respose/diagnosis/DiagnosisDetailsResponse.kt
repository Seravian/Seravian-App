package com.seravian.core_chat.data.dto.respose.diagnosis

import com.seravian.core_chat.domain.models.Diagnosis
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisDetailsResponse(
    val id: Long,
    val description: String?,
    val requestedAtUtc: String,
    val completedAtUtc: String?
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        description = description,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc
    )
}
