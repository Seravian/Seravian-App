package com.seravian.core_verification.data.dto.response

import com.seravian.core_verification.domain.Attachment
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentResponse(
    val id: String,
    val fileName: String
) {
    fun extractAttachment(): Attachment {
        return Attachment(id, fileName)
    }
}
