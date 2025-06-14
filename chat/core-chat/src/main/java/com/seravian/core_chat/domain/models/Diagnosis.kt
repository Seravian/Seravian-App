package com.seravian.core_chat.domain.models

import com.seravian.core_chat.data.entity.DiagnosisEntity

data class Diagnosis(
    val id: Long,
    val requestedAtUtc: String,
    val completedAtUtc: String?,
    val diagnosedProblem: String?,
    val reasoning: String?,
    val prescriptions: List<String>?,
    val failureReason: String?
) {
    fun toEntity(chatId: String) = DiagnosisEntity(
        id = id,
        chatId = chatId,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc,
        diagnosedProblem = diagnosedProblem,
        reasoning = reasoning,
        prescriptions = prescriptions,
        failureReason = failureReason
    )
}
