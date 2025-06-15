package com.seravian.core_verification.domain

import kotlinx.serialization.Serializable

@Serializable
data class WorkingTimeSlot(
    val from: String,
    val to: String
)
