package com.seravian.feat_chat.presentation.models

import com.seravian.core_ui.utils.formatDateTime
import com.seravian.core_chat.domain.models.Diagnosis

data class DiagnosisDetailsUI(
    val id: Long = -1,
    val requestedAtUtc: String = "5 June, 2025",
    val completedAtUtc: String? = null,
    val diagnosedProblem: String? = null,
    val reasoning: String? = null,
    val prescriptions: List<String>? = null,
    val failureReason: String? = null
)

fun Diagnosis.toDetailsUI() = DiagnosisDetailsUI(
    id = id,
    requestedAtUtc = formatDateTime(requestedAtUtc) ?: "",
    completedAtUtc = formatDateTime(completedAtUtc),
    diagnosedProblem = diagnosedProblem,
    reasoning = reasoning,
    prescriptions = prescriptions,
    failureReason = failureReason
)