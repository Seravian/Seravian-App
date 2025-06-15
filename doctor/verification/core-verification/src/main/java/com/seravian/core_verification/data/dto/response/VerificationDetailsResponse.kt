package com.seravian.core_verification.data.dto.response

import com.seravian.core_verification.domain.Verification
import com.seravian.core_verification.domain.WorkingTimeSlot
import com.seravian.core_verification.domain.utils.Day
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus
import kotlinx.serialization.Serializable

@Serializable
data class VerificationDetailsResponse(
    val id: Long,
    val createdAtUtc: String,
    val status: Int,
    val title: Int,
    val description: String,
    val sessionPrice: Int,
    val doctorTimeZone: String,
    val nationality: String,
    val languages: List<String>,
    val workingSchedule: Map<Day, List<WorkingTimeSlot>>,
    val deletedAtUtc: String? = null,
    val reviewedAtUtc: String? = null,
    val rejectionNotes: String? = null,
    val attachments: List<AttachmentResponse> = emptyList(),
    val attachmentsNote: String? = null
) {
    fun extractVerification(): Verification {
        return Verification(
            id = id,
            requestedAtUtc = createdAtUtc,
            status = VerificationStatus.entries[status],
            title = DoctorTitle.entries[title],
            description = description,
            sessionPrice = sessionPrice,
            doctorTimeZone = doctorTimeZone,
            nationality = nationality,
            languages = languages,
            workingSchedule = workingSchedule,
            deletedAtUtc = deletedAtUtc,
            reviewedAtUtc = reviewedAtUtc,
            rejectionNotes = rejectionNotes,
            attachments = attachments.map { it.extractAttachment() },
            attachmentsNote = attachmentsNote
        )
    }
}