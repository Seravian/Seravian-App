package com.seravian.core_verification.domain.utils

enum class VerificationStatus(val normalized: String) {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DELETED("Deleted")
}