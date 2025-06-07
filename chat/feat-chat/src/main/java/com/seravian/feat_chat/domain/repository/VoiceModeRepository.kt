package com.seravian.feat_chat.domain.repository

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.request.FetchAIAudioRequest
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