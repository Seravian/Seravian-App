package com.seravian.core_chat.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ClientRequest(
    val message: String
)
