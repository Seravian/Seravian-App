package com.seravian.core_chat.data.dto.request.diagnosis

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisDeletionRequest(
    val chatDiagnosisId: Long
)
