package com.seravian.feat_chat.presentation.viewModel.voice

import androidx.compose.runtime.Immutable
import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.domain.models.Audio

@Immutable
data class VoiceState(
    val isStreamingVoice: Boolean = false,
    val isMuted: Boolean = false,
    val voiceAmplitude: Float = 900f,
    val lastAudioId: Long? = null,
    val isWaitingForResponse: Boolean = false,
    val voiceUploadResult: EmptyResult<NetworkError> ?= null,
    val receivedAIAudioResult: NetworkResult<Audio, NetworkError> ?= null
)
