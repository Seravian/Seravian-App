package com.seravian.feat_chat.domain.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.voice.FetchAIAudioRequest
import com.seravian.core_chat.domain.models.Audio

interface VoiceModeRepository {
    suspend fun sendCapturedVoice(
        capturedVoice: ByteArray,
        chatId: String
    ): EmptyResult<NetworkError>

    suspend fun receiveAIAudioResponse(callback: (NetworkResult<Audio, NetworkError>) -> Unit)

    suspend fun fetchAIAudio(
        fetchRequest: FetchAIAudioRequest
    ): NetworkResult<Audio, NetworkError>
}