package com.seravian.feat_chat.presentation.screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.utils.toString
import com.meticha.permissions_compose.AppPermission
import com.meticha.permissions_compose.PermissionLifeCycleCheckEffect
import com.meticha.permissions_compose.rememberAppPermissionState
import com.seravian.feat_chat.R
import com.seravian.feat_chat.presentation.components.PulseCircle
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceAction
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceModeViewModel
import com.seravian.feat_chat.presentation.viewModel.voice.VoiceState

@Composable
fun VoiceModeScreen(
    navigateBack: () -> Unit,
) {
    val permissions = rememberAppPermissionState(
        permissions = listOf(
            AppPermission(
                permission = Manifest.permission.RECORD_AUDIO,
                description = "Microphone access is needed for voice recording and streaming. Please grant this permission.",
                isRequired = true
            )
        )
    )

    PermissionLifeCycleCheckEffect(permissions)

    BaseScreen<VoiceModeViewModel>(
        onPhysicalBack = { viewModel ->
            viewModel.voiceAction(VoiceAction.LeaveVoiceMode)
            navigateBack()
        },
        enableLifecycleObservation = true,
        onPauseAction = { viewModel ->
            viewModel.voiceAction(VoiceAction.StopStreaming)
            viewModel.voiceAction(VoiceAction.StopCollectingAIAudio(true))
        },
        onResumeAction = { viewModel ->
            if (permissions.allRequiredGranted()) {
                viewModel.voiceAction(VoiceAction.StartStreaming)
                viewModel.voiceAction(VoiceAction.StartCollectingAIAudio)
            } else {
                permissions.requestPermission()
            }
        }
    ) { viewModel ->
        val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()

        VoiceModeContent(
            voiceState = voiceState,
            voiceAction = {
                when(it) {
                    is VoiceAction.LeaveVoiceMode -> navigateBack()
                    else -> {}
                }
                viewModel.voiceAction(it)
            },
            baseAction = viewModel::baseAction,
        )
    }
}

@Composable
private fun VoiceModeContent(
    voiceState: VoiceState,
    voiceAction: (VoiceAction) -> Unit,
    baseAction: (BaseAction) -> Unit
) {
    val context = LocalContext.current

    voiceState.voiceUploadResult?.onError {
        baseAction(BaseAction.ShowErrorMessage(
            errorMessage = it.errorType?.toString(context) ?: ""
        ))
    }

    voiceState.receivedAIAudioResult
        ?.onError {
            baseAction(BaseAction.ShowErrorMessage(
                errorMessage = it.errorType?.toString(context) ?: "",
                dismissAction = {
                    voiceAction(VoiceAction.RestartStreaming)
                }
            ))
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                isVisible = true,
                isSideDestination = true,
                isActionEnabled = false,
                title = voiceState.currentChat?.title ?: "Seravian",
                navigateBack = {
                    voiceAction(VoiceAction.LeaveVoiceMode)
                }
            )
        },
    ) { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PulseCircle(
                amplitude = voiceState.voiceAmplitude,
                icon = painterResource(R.drawable.logo),
                isThinking = !voiceState.isStreamingVoice && voiceState.isWaitingForResponse,
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .align(Alignment.Center)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .align(Alignment.BottomCenter)
            ) {
                if (voiceState.isMuted) {
                    FilledIconButton(
                        onClick = {
                            voiceAction(VoiceAction.ChangeMicState)
                        },
                        enabled = voiceState.isStreamingVoice && !voiceState.isWaitingForResponse,
                        colors = IconButtonDefaults.filledIconButtonColors().copy(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_voice_mode_off),
                            contentDescription = stringResource(R.string.muted),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(7.dp)
                        )
                    }
                } else {
                    FilledIconButton(
                        onClick = {
                            voiceAction(VoiceAction.ChangeMicState)
                        },
                        enabled = voiceState.isStreamingVoice && !voiceState.isWaitingForResponse,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_voice_mode),
                            contentDescription = stringResource(R.string.unmuted),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(7.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = false)
@Composable
private fun VoiceModeContentPreview() {
    AppTheme {
        VoiceModeContent(
            voiceState = VoiceState(isMuted = true, voiceAmplitude = 10000f),
            voiceAction = {},
            baseAction = {}
        )
    }
}