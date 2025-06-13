package com.seravian.core_chat.domain.models

import com.seravian.core_chat.data.entity.DiagnosisEntity

data class Diagnosis(
    val id: Long,
    val description: String?,
    val requestedAtUtc: String,
    val completedAtUtc: String?
) {
    fun toEntity(chatId: String) = DiagnosisEntity(
        id = id,
        chatId = chatId,
        description = description,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc,
    )
}
