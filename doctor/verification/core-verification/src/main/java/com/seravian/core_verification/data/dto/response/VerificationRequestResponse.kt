package com.seravian.core_verification.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class VerificationRequestResponse(
    val verificationRequestId: Long
)
