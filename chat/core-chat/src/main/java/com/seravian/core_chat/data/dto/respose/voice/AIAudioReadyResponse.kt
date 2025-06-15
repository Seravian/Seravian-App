package com.seravian.core_chat.data.dto.respose.voice

import com.seravian.core_chat.data.dto.request.voice.FetchAIAudioRequest
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
