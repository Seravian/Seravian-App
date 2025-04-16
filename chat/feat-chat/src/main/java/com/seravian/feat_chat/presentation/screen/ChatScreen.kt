package com.seravian.feat_chat.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.greenvenom.core_ui.presentation.BaseScreen
import com.seravian.feat_chat.R
import com.seravian.feat_chat.presentation.utils.ChatToolbar
import com.seravian.feat_chat.presentation.viewModel.ChatViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.greenvenom.core_ui.theme.bluePrimary
import com.seravian.core_chat.domain.entity.Room
import com.seravian.feat_chat.presentation.utils.ChatInputTextField
import com.seravian.feat_chat.presentation.utils.MessagesLazyColumn


@Composable
fun ChatScreen(
    //room: Room ,
     modifier: Modifier = Modifier) {
    BaseScreen<ChatViewModel> { viewModel ->
        LaunchedEffect(key1 = Unit) {
//            viewModel.room = room
        }

        Scaffold (topBar = {
            ChatToolbar(
                title = "room.name",
            )
        }) { innerPadding ->
            innerPadding
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    , verticalArrangement = Arrangement.Bottom
            ) {

                Card(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(.9f)
                        .fillMaxHeight(.8f),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    MessagesLazyColumn()

                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ChatInputTextField(state = viewModel.messageState)
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = {
                            //click to send a message
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = stringResource(R.string.send),
                            modifier = Modifier.padding(10.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.send),
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
    ChatScreen()
}