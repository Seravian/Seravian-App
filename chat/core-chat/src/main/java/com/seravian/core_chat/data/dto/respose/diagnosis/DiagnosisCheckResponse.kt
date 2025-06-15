package com.seravian.core_chat.data.dto.respose.diagnosis

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisCheckResponse(
    val isDiagnosing: Boolean
)
