package com.seravian.core_chat.data.dto.respose.diagnosis

import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisReadyResponse(
    val chatId: String,
    val chatDiagnoseId: Long,
)
