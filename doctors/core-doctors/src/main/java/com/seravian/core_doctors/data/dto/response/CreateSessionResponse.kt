package com.seravian.core_doctors.data.dto.response

import com.seravian.core_doctors.domain.models.Session
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionResponse(
    val sessionBookingId : String
){
    fun extractSession():Session{
        return Session(
            sessionBookingId = sessionBookingId
        )
    }
}
