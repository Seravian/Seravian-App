package com.seravian.core_chat.data.dto.respose.diagnosis

import com.seravian.core_chat.domain.models.Diagnosis
import kotlinx.serialization.Serializable

@Serializable
data class ChatDiagnosisResponse(
    val id: Long,
    val requestedAtUtc: String,
    val completedAtUtc: String?
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        description = null,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc
    )
}
