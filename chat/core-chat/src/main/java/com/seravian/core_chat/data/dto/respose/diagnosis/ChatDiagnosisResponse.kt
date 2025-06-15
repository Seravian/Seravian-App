package com.seravian.core_chat.data.dto.respose.diagnosis

import com.seravian.core_chat.domain.models.Diagnosis
import kotlinx.serialization.Serializable

@Serializable
data class ChatDiagnosisResponse(
    val id: Long,
    val requestedAtUtc: String,
    val completedAtUtc: String?,
    val diagnosedProblem: String?,
    val failureReason: String?
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc,
        diagnosedProblem = diagnosedProblem,
        reasoning = null,
        prescriptions = null,
        failureReason = failureReason
    )
}
