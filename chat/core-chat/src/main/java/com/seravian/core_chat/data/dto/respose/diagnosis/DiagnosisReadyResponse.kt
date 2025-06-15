package com.seravian.core_chat.data.dto.respose.diagnosis

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisReadyResponse(
    val id: Long,
    val requestedAtUtc: String,
    val completedAtUtc: String,
    val diagnosedProblem: String?,
    val reasoning: String?,
    val prescriptions: List<String>?,
    val failureReason: String?
)
