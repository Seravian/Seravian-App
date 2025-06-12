package com.seravian.core_verification.domain

import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus

data class Verification(
    val id: Int,
    val requestedAtUtc: String,
    val attachments: List<Attachment> = emptyList(),
    val status: VerificationStatus,
    val title: DoctorTitle,
    val description: String,
    val sessionPrice: Int,
    val deletedAtUtc: String? = null,
    val reviewedAtUtc: String? = null,
    val rejectionNotes: String? = null
) {
    fun toDeleteRequest() = DeleteVerificationRequest(id)

    fun toDetailsRequest() = GetVerificationsRequest(id)
}
