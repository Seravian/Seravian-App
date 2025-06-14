package com.seravian.feat_chat.presentation.models

import com.greenvenom.core_ui.utils.formatDateTime
import com.seravian.core_chat.domain.models.Diagnosis

data class DiagnosisCardUI(
    val id: Long = -1,
    val requestedAtUtc: String = "5 June, 2025",
    val completedAtUtc: String? = null,
    val isFailed: Boolean = false
)

fun Diagnosis.toCardUI() = DiagnosisCardUI(
    id = id,
    requestedAtUtc = formatDateTime(requestedAtUtc) ?: "",
    completedAtUtc = formatDateTime(completedAtUtc),
    isFailed = failureReason != null && diagnosedProblem == null
)