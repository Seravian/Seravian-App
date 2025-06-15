package com.seravian.core_home.data.dto.response

import com.seravian.core_home.domain.DisordersAdvices
import kotlinx.serialization.Serializable

@Serializable
data class DisordersAdvicesResponse(
    val id: Int,
    val disorder: String,
    val advices: List<String>
) {
    fun extractDisordersAdvices(): DisordersAdvices {
        return DisordersAdvices(
            id = id,
            disorder = disorder,
            advices = advices
        )
    }
}
