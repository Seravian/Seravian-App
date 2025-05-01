package com.seravian.core_chat.data.dto.respose

import kotlinx.serialization.Serializable

@Serializable
data class ClientResponse(
    val message: String,
    val timestampUtc: String
)
