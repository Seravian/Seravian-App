package com.seravian.feat_chat.presentation.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.MessageType
import com.seravian.feat_chat.presentation.models.MessageUI
import com.seravian.feat_chat.R

@Composable
fun ReceivedMessageCard(
    message: MessageUI,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.align(Alignment.Bottom)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.chatbot_ic),
                modifier = Modifier
                    .size(40.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    colorScheme.surfaceVariant.copy(
                        alpha = 0.4f
                    )
                } else {
                    colorScheme.surfaceVariant
                },
                contentColor = if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                } else {
                    colorScheme.onSurface
                }
            ),
            modifier = Modifier
                .weight(1f, false)
        ) {
            Text(
                text = message.content,
                fontStyle = if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    FontStyle.Italic
                } else {
                    null
                },
                modifier = Modifier
                    .padding(12.dp)
                    .padding(start = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            ) {
                if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    Icon(
                        painter = painterResource(R.drawable.ic_mic),
                        contentDescription = stringResource(R.string.voice_mode_message),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        Spacer(
            modifier = Modifier
                .width(24.dp)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ReceivedMessageCardPreview() {
    AppTheme {
        ReceivedMessageCard(
            MessageUI(
                content = "jghvchjcvbnbvnbvjmhjmjkhgfkhjgkghkghkjhghghjhghjg,hjkghjkhlhjlhjghkjhl",
                timestamp = "02:38 AM",
                messageType = MessageType.VOICE_MODE_TEXT
            )
        )
    }
}