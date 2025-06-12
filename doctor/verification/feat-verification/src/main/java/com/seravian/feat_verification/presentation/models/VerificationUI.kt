package com.seravian.feat_verification.presentation.models

import com.greenvenom.core_ui.utils.formatDateTime
import com.seravian.core_verification.domain.Attachment
import com.seravian.core_verification.domain.Verification
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus

data class VerificationUI(
    val id: Int = 0,
    val requestedAtUtc: String = "",
    val attachments: List<Attachment> = emptyList(),
    val status: VerificationStatus = VerificationStatus.PENDING,
    val doctorTitle: DoctorTitle = DoctorTitle.PSYCHIATRIST,
    val description: String = "",
    val sessionPrice: String = "",
    val deletedAtUtc: String? = null,
    val reviewedAtUtc: String? = null,
    val rejectionNotes: String? = null
)

fun Verification.toVerificationUI(): VerificationUI {
    return VerificationUI(
        id = id,
        requestedAtUtc = formatDateTime(requestedAtUtc) ?: "",
        attachments = attachments,
        status = status,
        doctorTitle = title,
        description = description,
        sessionPrice = "$sessionPrice EGP",
        deletedAtUtc = formatDateTime(deletedAtUtc),
        reviewedAtUtc = formatDateTime(reviewedAtUtc),
        rejectionNotes = formatDateTime(rejectionNotes)
    )
}