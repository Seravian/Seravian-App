package com.seravian.feat_chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_chat.domain.MessageType
import com.seravian.feat_chat.presentation.models.MessageUI

@Composable
fun SentMessageCard(
    message: MessageUI,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .width(24.dp)
        )
        Card(
            shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    colorScheme.tertiaryContainer.copy(
                        alpha = 0.5f
                    )
                } else {
                    colorScheme.tertiaryContainer
                },
                contentColor = if (message.messageType == MessageType.VOICE_MODE_TEXT) {
                    colorScheme.onTertiaryContainer.copy(
                        alpha = 0.7f
                    )
                } else {
                    colorScheme.onTertiaryContainer
                }
            ),
            modifier = Modifier.weight(1f, false)
        ) {
            Column {
                Text(
                    text = message.content,
                    modifier = Modifier
                        .padding(12.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SentMessageCardPreview() {
    AppTheme {
        SentMessageCard(
            MessageUI(
                content = "Helgdgkidfgdfhfghshfgdthhfhjftgjfjhgfdjggfdhgfdshhfglo",
                timestamp = "02:38 AM",
                messageType = MessageType.TEXT
            )
        )
    }
}