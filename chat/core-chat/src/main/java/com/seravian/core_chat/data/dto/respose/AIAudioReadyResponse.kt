package com.seravian.core_chat.data.dto.respose

import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
import kotlinx.serialization.Serializable

@Serializable
data class AIAudioReadyResponse(
    val aiAudioId: Long,
    val chatId: String
) {
    fun extractFetchRequest(): FetchAIAudioRequest {
        return FetchAIAudioRequest(aiAudioId)
    }
}
