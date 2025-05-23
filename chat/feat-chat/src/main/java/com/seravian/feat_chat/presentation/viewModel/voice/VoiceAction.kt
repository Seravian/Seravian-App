package com.seravian.feat_chat.presentation.viewModel.voice

sealed interface VoiceAction {
    data object StartStreaming : VoiceAction
    data object StartCollectingAIAudio : VoiceAction
    data object ChangeMicState : VoiceAction
    data object BuildAudioPlayer : VoiceAction
    data object StopStreaming : VoiceAction
    data class StopCollectingAIAudio(val releaseAudioPlayer: Boolean) : VoiceAction
    data object ResetVoiceState : VoiceAction
    data object RestartStreaming : VoiceAction
    data object NavigateBack : VoiceAction
}