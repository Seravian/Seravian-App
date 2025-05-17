package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.utils.toString
import com.greenvenom.core_ui.components.FloatingButton
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Chat
import com.seravian.feat_chat.presentation.viewModel.ChatAction
import com.seravian.feat_chat.presentation.components.ChatListCard
import com.seravian.feat_chat.presentation.components.NewChatPopUp
import com.seravian.feat_chat.presentation.models.toChatUI
import com.seravian.feat_chat.presentation.viewModel.ChatState
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel

@Composable
fun ChatListScreen(
    navigateToChat: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<ChatViewModel>(
        onPhysicalBack = { viewModel ->
            navigateBack()
            viewModel.chatAction(ChatAction.ClearChatResults)
        },
        modifier = modifier
    ) { viewModel ->
        val state by viewModel.chatState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.chatAction(ChatAction.GetChats) }

        ChatListContent(
            chatState = state,
            chatAction = viewModel::chatAction,
            baseAction = viewModel::baseAction,
            navigateToChat = navigateToChat
        )
    }
}

@Composable
private fun ChatListContent(
    chatState: ChatState,
    chatAction: (ChatAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    navigateToChat: (String) -> Unit
) {
    val context = LocalContext.current
    var popupState by rememberSaveable { mutableStateOf(false) }
    var isEdit by rememberSaveable { mutableStateOf(false) }
    var chatId by rememberSaveable { mutableStateOf("") }
    var newChatTitle by rememberSaveable { mutableStateOf("") }

    chatState.joinChatResult
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString() ?: ""))
        }

    chatState.getChatsResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
            chatAction(ChatAction.ClearChatResults)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: ""))
        }

    chatState.createChatResult
        ?.onSuccess { response ->
            popupState = false
            newChatTitle = ""
            baseAction(BaseAction.HideLoading)
            navigateToChat(response.id)
            chatAction(ChatAction.ClearChatResults)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: ""))
        }

    chatState.deleteChatResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
            popupState = false
            newChatTitle = ""
            chatId = ""
            isEdit = false
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: ""))
        }

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = false,
            )
        },
        floatingActionButton = {
            FloatingButton(
                isVisible = true,
                onClick = { popupState = true },
                modifier = Modifier.size(64.dp)
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = 0.dp,
            start = 16.dp,
            end = 16.dp
        )

        if (popupState) {
            NewChatPopUp(
                value = newChatTitle,
                onValueChange = { newChatTitle = it },
                onCreateChat = {
                    chatAction(ChatAction.CreateChat(newChatTitle))
                    baseAction(BaseAction.ShowLoading)
                },
                onEditChat = {
                    chatAction(ChatAction.EditChat(chatId, newChatTitle))
                    baseAction(BaseAction.ShowLoading)
                },
                onDelete = { chatAction(ChatAction.DeleteChat(chatId)) },
                onDismiss = {
                    popupState = false
                    isEdit = false
                    newChatTitle = ""
                    chatId = ""
                },
                isEdit = isEdit
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(modifiedPadding)
                .fillMaxSize()
        ) {
            items(
                items = chatState.chatsList.map { chat -> chat.toChatUI() },
                key = { it.id }
            ) { chat ->
                ChatListCard(
                    chat = chat,
                    onClick = { navigateToChat(chat.id) },
                    onEdit = { id, title ->
                        isEdit = true
                        newChatTitle = title
                        chatId = id
                        popupState = true
                    },
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
            navigateToChat = {},
            chatAction = {},
            baseAction = {}
        )
    }
}