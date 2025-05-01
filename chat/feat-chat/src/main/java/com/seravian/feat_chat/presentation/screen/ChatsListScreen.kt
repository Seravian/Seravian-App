package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Chat
import com.seravian.feat_chat.presentation.components.ChatListCard
import com.seravian.feat_chat.presentation.models.toChatUI
import com.seravian.feat_chat.presentation.viewModel.ChatState
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel

@Composable
fun ChatListScreen(
    navigateToChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<ChatViewModel> { viewModel ->
        val state by viewModel.chatState.collectAsStateWithLifecycle()

        ChatListContent(
            chatState = state,
            navigateToChat = navigateToChat
        )
    }
}

@Composable
private fun ChatListContent(
    chatState: ChatState,
    navigateToChat: (String) -> Unit
) {
    Scaffold(

    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = 0.dp,
            start = 16.dp,
            end = 16.dp
        )

        LazyColumn(
            modifier = Modifier
                .padding(modifiedPadding)
                .fillMaxSize()
        ) {
            items(
                chatState.chatsList?.map { chat ->
                    chat.toChatUI()
                } ?: emptyList(),
            ) { chat ->
                ChatListCard(
                    chat = chat,
                    onClick = { navigateToChat(chat.id) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun ChatListScreenPreview() {
    AppTheme {
        ChatListContent(
            chatState = ChatState(
                chatsList = listOf(
                    Chat(
                        id = "1",
                        title = "ADHD Analysis",
                        createdAt = "2023-06-05T14:30:40Z",
                    ),
                    Chat(
                        id = "2",
                        title = "ADHD Analysis",
                        createdAt = "2023-06-05T14:30:40Z",
                    ),
                    Chat(
                        id = "3",
                        title = "ADHD Analysis",
                        createdAt = "2023-06-05T14:30:40Z",
                    ),
                )
            ),
            navigateToChat = {}
        )
    }
}