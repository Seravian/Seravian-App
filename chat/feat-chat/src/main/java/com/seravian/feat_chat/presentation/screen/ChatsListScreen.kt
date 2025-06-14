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
import com.seravian.feat_chat.presentation.components.chats_list.ChatListCard
import com.seravian.feat_chat.presentation.components.chats_list.NewChatPopUp
import com.seravian.feat_chat.presentation.models.toChatUI
import com.seravian.feat_chat.presentation.viewModel.chats_list.ChatsListAction
import com.seravian.feat_chat.presentation.viewModel.chats_list.ChatsListState
import com.seravian.feat_chat.presentation.viewModel.chats_list.ChatsListViewModel

@Composable
fun ChatListScreen(
    navigateToChat: (String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<ChatsListViewModel>(
        onPhysicalBack = { viewModel ->
            navigateBack()
        },
        modifier = modifier
    ) { viewModel ->
        val state by viewModel.chatsListState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) { viewModel.chatsListAction(ChatsListAction.GetChats) }

        ChatListContent(
            chatsListState = state,
            chatsListAction = {
                when (it) {
                    is ChatsListAction.NavigateToChat -> navigateToChat(it.chat.id)
                    else -> {}
                }
                viewModel.chatsListAction(it)
            },
            baseAction = viewModel::baseAction,
        )
    }
}

@Composable
private fun ChatListContent(
    chatsListState: ChatsListState,
    chatsListAction: (ChatsListAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
) {
    val context = LocalContext.current
    var popupState by rememberSaveable { mutableStateOf(false) }
    var isEdit by rememberSaveable { mutableStateOf(false) }
    var chatId by rememberSaveable { mutableStateOf("") }
    var newChatTitle by rememberSaveable { mutableStateOf("") }

    chatsListState.getChatsResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: ""))
        }

    chatsListState.createChatResult
        ?.onSuccess { response ->
            popupState = false
            newChatTitle = ""
            baseAction(BaseAction.HideLoading)
            chatsListAction(ChatsListAction.ClearChatOperationResults)
            chatsListAction(ChatsListAction.NavigateToChat(response))
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: "") {
                chatsListAction(ChatsListAction.ClearChatOperationResults)
            })
        }

    chatsListState.deleteChatResult
        ?.onSuccess {
            baseAction(BaseAction.HideLoading)
            popupState = false
            newChatTitle = ""
            chatId = ""
            isEdit = false
            chatsListAction(ChatsListAction.ClearChatOperationResults)
        }
        ?.onError {
            baseAction(BaseAction.HideLoading)
            baseAction(BaseAction.ShowErrorMessage(it.errorType?.toString(context) ?: "") {
                chatsListAction(ChatsListAction.ClearChatOperationResults)
            })
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
                onClick = {
                    chatsListAction(ChatsListAction.CreateChat)
                    baseAction(BaseAction.ShowLoading)
                },
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
                    chatsListAction(ChatsListAction.CreateChat)
                    baseAction(BaseAction.ShowLoading)
                },
                onEditChat = {
                    chatsListAction(ChatsListAction.EditChat(chatId, newChatTitle))
                    baseAction(BaseAction.ShowLoading)
                },
                onDelete = { chatsListAction(ChatsListAction.DeleteChat(chatId)) },
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
                items = chatsListState.chatsList.map { chat -> chat.toChatUI() },
                key = { it.id }
            ) { chat ->
                ChatListCard(
                    chat = chat,
                    onClick = {
                        chatsListAction(ChatsListAction.NavigateToChat(
                            chatsListState.chatsList.find { it.id == chat.id } ?: Chat()
                        ))
                    },
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
            chatsListState = ChatsListState(
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
            chatsListAction = {},
            baseAction = {}
        )
    }
}