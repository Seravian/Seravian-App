package com.seravian.core_verification.domain

import com.seravian.core_verification.data.dto.request.DeleteVerificationRequest
import com.seravian.core_verification.data.dto.request.GetVerificationsRequest
import com.seravian.core_verification.data.dto.request.VerificationRequest
import com.seravian.core_verification.domain.utils.Day
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.utils.VerificationStatus

data class Verification(
    val id: Long,
    val requestedAtUtc: String,
    val status: VerificationStatus,
    val title: DoctorTitle,
    val description: String,
    val sessionPrice: Int,
    val doctorTimeZone: String,
    val nationality: String,
    val languages: List<String>,
    val workingSchedule: Map<Day, List<WorkingTimeSlot>>,
    val deletedAtUtc: String? = null,
    val reviewedAtUtc: String? = null,
    val rejectionNotes: String? = null,
    val attachments: List<Attachment> = emptyList(),
    val attachmentsNote: String? = null
) {
    fun toDeleteRequest() = DeleteVerificationRequest(id)

    fun toDetailsRequest() = GetVerificationsRequest(id)
}
