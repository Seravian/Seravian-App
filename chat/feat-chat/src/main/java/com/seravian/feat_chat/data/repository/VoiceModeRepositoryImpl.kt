package com.seravian.feat_chat.data.repository

import com.seravian.core_network.data.EmptyResult
import com.seravian.core_network.data.ErrorType
import com.seravian.core_network.data.NetworkError
import com.seravian.core_network.data.NetworkResult
import com.seravian.core_network.data.map
import com.seravian.core_chat.data.dto.request.voice.FetchAIAudioRequest
import com.seravian.core_chat.data.dto.request.voice.UploadVoiceRequest
import com.seravian.core_chat.domain.models.Audio
import com.seravian.feat_chat.domain.ChatBotRemoteDataSource
import com.seravian.feat_chat.domain.repository.VoiceModeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex

class VoiceModeRepositoryImpl(
    private val seravianChatBotDataSource: ChatBotRemoteDataSource,
    private val chatBotStateRepository: ChatBotStateRepository
): VoiceModeRepository {
    private val audioDownloadLock: Mutex = Mutex()
    private var downloadExecution: Deferred<NetworkResult<Audio, NetworkError>>? = null

    private var lastNotifiedAudio: Long = -1

    override suspend fun sendCapturedVoice(
        capturedVoice: ByteArray,
        chatId: String
    ): EmptyResult<NetworkError> {
        val uploadResult = seravianChatBotDataSource.uploadUserVoice(
            UploadVoiceRequest(capturedVoice, chatId)
        )
        chatBotStateRepository.checkResponseProcessing()

        return uploadResult
    }

    override suspend fun receiveAIAudioResponse(callback: (NetworkResult<Audio, NetworkError>) -> Unit) {
        seravianChatBotDataSource.receiveAIAudioReadyResponse { aiAudioReadyResponse ->
            lastNotifiedAudio = aiAudioReadyResponse.aiAudioId
            val fetchingResult = downloadAudio(
                aiAudioReadyResponse.extractFetchRequest()
            )
            callback(fetchingResult)
        }
    }

    override suspend fun fetchAIAudio(
        fetchRequest: FetchAIAudioRequest
    ): NetworkResult<Audio, NetworkError> {
        when {
            fetchRequest.aiAudioId == lastNotifiedAudio && downloadExecution != null -> {
                return NetworkResult.Error(NetworkError(ErrorType.TOO_MANY_REQUESTS))
            }
            fetchRequest.aiAudioId != lastNotifiedAudio && downloadExecution == null -> {
                lastNotifiedAudio = fetchRequest.aiAudioId
                return downloadAudio(fetchRequest)
            }
            fetchRequest.aiAudioId == lastNotifiedAudio && downloadExecution == null -> {
                lastNotifiedAudio = fetchRequest.aiAudioId
                return downloadAudio(fetchRequest)
            }
            else -> { return NetworkResult.Error(NetworkError(ErrorType.UNKNOWN_ERROR)) }
        }
    }

    private suspend fun downloadAudio(
        fetchRequest: FetchAIAudioRequest
    ): NetworkResult<Audio, NetworkError> {
        if (!audioDownloadLock.tryLock()) {
            downloadExecution?.let {
                return NetworkResult.Error(NetworkError(ErrorType.TOO_MANY_REQUESTS))
            }
        }

        return try {
            val deferred = CoroutineScope(Dispatchers.IO).async {
                seravianChatBotDataSource.fetchAIAudioResponse(fetchRequest).map { response ->
                    response.extractAudio(fetchRequest.aiAudioId)
                }
            }
            downloadExecution = deferred

            deferred.await()
        } finally {
            chatBotStateRepository.checkResponseProcessing()
            audioDownloadLock.unlock()
            downloadExecution = null
        }
    }
}