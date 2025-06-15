package com.seravian.feat_verification.presentation.viewmodel.requests

import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.WorkingTimeSlot
import com.seravian.core_verification.domain.utils.DoctorTitle

sealed interface VerificationRequestsAction {
    data class SendVerificationRequest(
        val doctorTitle: DoctorTitle,
        val description: String,
        val sessionPrice: Int,
        val doctorTimeZone: String,
        val nationality: String,
        val languages: List<String>,
        val workingSchedule: Map<Int, List<WorkingTimeSlot>>,
        val attachments: List<FileAttachment>,
        val attachmentsNote: String?
    ): VerificationRequestsAction
    data object NavigateBack: VerificationRequestsAction
}