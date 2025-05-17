package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_chat.presentation.viewModel.ChatAction
import com.seravian.feat_chat.presentation.viewModel.ChatState
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import androidx.compose.runtime.getValue

@Composable
fun VoiceModeScreen(
    navigateBack: () -> Unit,
) {
    BaseScreen<ChatViewModel>(
        onPhysicalBack = { viewModel ->
            viewModel.chatAction(ChatAction.StopStreaming)
            navigateBack()
        },
    ) { viewModel ->
        val chatState by viewModel.chatState.collectAsStateWithLifecycle()

        VoiceModeContent(
            chatState = chatState,
            chatAction = {
                when(it) {
                    is ChatAction.NavigateBack -> navigateBack()
                    else -> {}
                }
                viewModel.chatAction(it)
            },
            baseAction = viewModel::baseAction,
        )
    }
}

@Composable
private fun VoiceModeContent(
    chatState: ChatState,
    chatAction: (ChatAction) -> Unit,
    baseAction: (BaseAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                isVisible = true,
                isSideDestination = true,
                isActionEnabled = false,
                title = chatState.currentChat?.title ?: "Seravian",
                navigateBack = { chatAction(ChatAction.NavigateBack) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}

@Preview
@Composable
private fun VoiceModeContentPreview() {
    AppTheme {
        VoiceModeContent(
            chatState = ChatState(),
            chatAction = {},
            baseAction = {}
        )
    }
}