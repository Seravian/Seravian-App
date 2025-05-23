package com.seravian.feat_chat.presentation.viewModel.voice

import com.greenvenom.core_network.data.EmptyResult
import com.greenvenom.core_network.data.NetworkError
import com.greenvenom.core_network.data.NetworkResult
import com.seravian.core_chat.data.dto.respose.AIAudioResponse

data class VoiceState(
    val isStreamingVoice: Boolean = false,
    val isMuted: Boolean = false,
    val voiceAmplitude: Float = 900f,
    val voiceUploadResult: EmptyResult<NetworkError> ?= null,
    val receivedAIAudioResult:NetworkResult<AIAudioResponse, NetworkError> ?= null
)
