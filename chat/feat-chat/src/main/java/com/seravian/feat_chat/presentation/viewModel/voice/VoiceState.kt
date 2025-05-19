package com.seravian.feat_chat.presentation.viewModel.voice

data class VoiceState(
    val isStreamingVoice: Boolean = true,
    val isMuted: Boolean = false,
    val voiceAmplitude: Float = 900f
)
