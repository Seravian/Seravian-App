package com.seravian.feat_verification.presentation.models

import com.seravian.core_ui.utils.formatDateTime
import com.seravian.core_verification.domain.Attachment
import com.seravian.core_verification.domain.Verification
import com.seravian.core_verification.domain.WorkingTimeSlot
import com.seravian.core_verification.domain.utils.Day
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus

data class VerificationUI(
    val id: Long = 0,
    val requestedAt: String = "",
    val status: VerificationStatus = VerificationStatus.PENDING,
    val doctorTitle: DoctorTitle = DoctorTitle.PSYCHIATRIST,
    val description: String = "",
    val sessionPrice: String = "",
    val doctorTimeZone: String = "",
    val nationality: String = "",
    val languages: List<String> = emptyList(),
    val workingSchedule: Map<Day, List<WorkingTimeSlot>> = emptyMap(),
    val deletedAt: String? = null,
    val reviewedAt: String? = null,
    val rejectionNotes: String? = null,
    val attachments: List<Attachment> = emptyList(),
    val attachmentsNote: String? = ""
)

fun Verification.toVerificationUI(): VerificationUI {
    return VerificationUI(
        id = id,
        requestedAt = formatDateTime(requestedAtUtc) ?: "",
        status = status,
        doctorTitle = title,
        description = description,
        sessionPrice = "$sessionPrice EGP",
        doctorTimeZone = doctorTimeZone,
        nationality = nationality,
        languages = languages,
        workingSchedule = workingSchedule,
        deletedAt = formatDateTime(deletedAtUtc),
        reviewedAt = formatDateTime(reviewedAtUtc),
        rejectionNotes = formatDateTime(rejectionNotes),
        attachments = attachments,
        attachmentsNote = attachmentsNote
    )
}