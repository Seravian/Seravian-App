package com.seravian.feat_chat.presentation.viewModel.voice

sealed interface VoiceAction {
    data object StartStreaming : VoiceAction
    data object ChangeMicState : VoiceAction
    data object StopStreaming : VoiceAction
    data object ResetVoiceState : VoiceAction
    data object NavigateBack : VoiceAction
}