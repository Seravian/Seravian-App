package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.feat_chat.R
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.presentation.ChatAction
import com.seravian.feat_chat.presentation.components.ChatInputTextField
import com.seravian.feat_chat.presentation.components.ReceivedMessageCard
import com.seravian.feat_chat.presentation.components.SentMessageCard
import com.seravian.feat_chat.presentation.models.toMessageUI
import com.seravian.feat_chat.presentation.viewModel.ChatState

@Composable
fun ChatScreen(
    chatId: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<ChatViewModel> { viewModel ->
        val chatState by viewModel.chatState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.chatAction(ChatAction.GetChatMessages(chatId))
            viewModel.baseAction(BaseAction.ShowLoading)
        }

        DisposableEffect(Unit) { onDispose { viewModel.chatAction(ChatAction.LeaveChat) } }

        ChatScreenContent(
            chatState = chatState,
            chatAction = {
                when(it) {
                    is ChatAction.NavigateBack -> navigateBack()
                    else -> {}
                }
                viewModel.chatAction(it)
            },
            baseAction = viewModel::baseAction,
            modifier = modifier
        )
    }
}

@Composable
private fun ChatScreenContent(
    chatState: ChatState,
    chatAction: (ChatAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var input by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(chatState.messagesList.size) {
        if (chatState.messagesList.isEmpty()) return@LaunchedEffect
        listState.animateScrollToItem(chatState.messagesList.lastIndex)
    }

    chatState.joinChatResult
        ?.onSuccess {
            chatAction(ChatAction.ClearChatResults)
            baseAction(BaseAction.HideLoading)
        }
        ?.onError {
            chatAction(ChatAction.NavigateBack)
        }

    Scaffold (
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = false,
                title = chatState.currentChat?.title ?: "Seravian",
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(
                    items = chatState.messagesList.map { message -> message.toMessageUI() },
                    key = { it.id }
                ) { message ->
                    if (!message.isAI) {
                        //sent
                        SentMessageCard(message = message)
                    } else {
                        //received
                        ReceivedMessageCard(message = message)
                    }
                }
            }
            Card(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ChatInputTextField(
                        input = input,
                        onValueChange = { input = it },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton (
                        onClick = {
                            chatAction(ChatAction.SendMessage(input))
                            input = ""
                        },
                        enabled = chatState.messagesList.lastOrNull()?.isAI ?: true,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.send_ic),
                            contentDescription = stringResource(
                                R.string.send_message_button_icon
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    AppTheme {
        ChatScreenContent(
            chatState = ChatState(
                messagesList = mutableListOf(
                    Message(id = Pair(1, null), isAI = false, content = "Hello", timestamp = "2023-06-05T14:30:40Z"),
                    Message(id = Pair(2, null), isAI = true, content = "gfhgfhfggfdkjghfdgudfiuhgdfgiufdhigudrhduihjnifgudnhiufgnhuidfgnhiudfnsghfduhiugfdgfiuhf", timestamp = "2023-06-05T14:30:45Z"),
                    Message(id = Pair(3, null), isAI = true, content = "Hello", timestamp = "2023-06-05T14:30:50Z")
                )
            ),
            chatAction = {},
            baseAction = {}
        )
    }
}