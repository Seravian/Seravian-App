package com.seravian.core_chat.data.dto.respose.diagnosis

import com.seravian.core_chat.domain.models.Diagnosis
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisDetailsResponse(
    val id: Long,
    val requestedAtUtc: String,
    val completedAtUtc: String?,
    val diagnosedProblem: String?,
    val reasoning: String?,
    val prescriptions: List<String>?,
    val failureReason: String?
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc,
        diagnosedProblem = diagnosedProblem,
        reasoning = reasoning,
        prescriptions = prescriptions,
        failureReason = failureReason
    )
}
