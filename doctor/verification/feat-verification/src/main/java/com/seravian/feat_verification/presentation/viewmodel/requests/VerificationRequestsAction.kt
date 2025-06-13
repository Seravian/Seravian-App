package com.seravian.feat_verification.presentation.viewmodel.requests

import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.utils.DoctorTitle

sealed interface VerificationRequestsAction {
    data class SendVerificationRequest(
        val doctorTitle: DoctorTitle,
        val description: String,
        val sessionPrice: Int,
        val attachments: List<FileAttachment>
    ): VerificationRequestsAction
    data object NavigateBack: VerificationRequestsAction
}