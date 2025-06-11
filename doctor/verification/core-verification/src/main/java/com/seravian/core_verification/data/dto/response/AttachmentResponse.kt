package com.seravian.core_verification.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentResponse(
    val id: String,
    val fileName: String
)
