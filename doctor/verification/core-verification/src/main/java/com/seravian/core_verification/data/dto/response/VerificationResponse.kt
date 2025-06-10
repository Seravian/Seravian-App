package com.seravian.core_verification.data.dto.response

import com.seravian.core_verification.domain.Verification
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class VerificationResponse(
    val id: Int,
    val requestedAtUtc: String,
    val attachments: List<AttachmentResponse> = emptyList(),
    val status: Int,
    val title: Int,
    val description: String,
    val deletedAtUtc: String? = null,
    val reviewedAtUtc: String? = null,
    val rejectionNotes: String? = null
) {
    fun extractVerification(): Verification {
        return Verification(
            id = id,
            requestedAtUtc = requestedAtUtc,
            attachments = attachments,
            status = VerificationStatus.entries[status],
            title = DoctorTitle.entries[title],
            description = description,
            deletedAtUtc = deletedAtUtc,
            reviewedAtUtc = reviewedAtUtc,
            rejectionNotes = rejectionNotes
        )
    }
}