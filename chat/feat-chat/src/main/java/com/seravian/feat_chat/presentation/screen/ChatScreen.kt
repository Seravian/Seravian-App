package com.seravian.feat_chat.presentation.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.models.Message
import com.seravian.feat_chat.presentation.components.ChatInputTextField
import com.seravian.feat_chat.presentation.components.ReceivedMessageCard
import com.seravian.feat_chat.presentation.components.SentMessageCard
import com.seravian.feat_chat.presentation.models.toMessageUI
import com.seravian.feat_chat.presentation.viewModel.ChatState

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier
) {
    val userId = "suid3"

    BaseScreen<ChatViewModel> { viewModel ->
        val chatState by viewModel.chatState.collectAsStateWithLifecycle()

        ChatScreenContent(
            chatState = chatState,
            userId = userId,
            modifier = modifier
        )
    }
}

@Composable
private fun ChatScreenContent(
    chatState: ChatState,
    userId: String,
    modifier: Modifier = Modifier
) {
    var input by rememberSaveable { mutableStateOf("") }

    Scaffold (
        topBar = {
            TopAppBar(
                isVisible = true,
                isActionEnabled = false,
                isSideDestination = false,
            )
        },
        bottomBar = {
            ElevatedCard(
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
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
                        modifier = Modifier.size(48.dp),
                        onClick = {
                            //click to send a message
                        },
                        shape = RoundedCornerShape(50),
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            items(
                items = chatState.messagesList.map { message -> message.toMessageUI() },
                key = { message -> message.id }
            ) { message ->
                if (userId == message.senderId) {
                    //sent
                    SentMessageCard(message = message)

                } else {
                    //received
                    ReceivedMessageCard(message = message)
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
                messagesList = listOf(
                    Message(id = "uuid1", content = "Hello", senderName = "kareem", dateTime = 2333232),
                    Message(id = "uuid2", content = "Hello", senderId = "suid3", dateTime = 2333232)
                )
            ),
            userId = "suid3"
        )
    }
}