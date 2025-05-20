package com.seravian.feat_chat.presentation.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.align(Alignment.Bottom)
        ) {
            Icon(
                painter = painterResource(R.drawable.chatbot_ic),
                contentDescription = stringResource(R.string.chatbot_ic),
                modifier = Modifier
                    .padding(6.dp)
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp),
            modifier = Modifier
                .weight(1f, false)
        ) {
            Text(
                text = message.content,
                modifier = Modifier
                    .padding(12.dp)
                    .padding(start = 4.dp)
            )
            Text(
                text = message.timestamp,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                    .align(Alignment.End)
            )
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
    ReceivedMessageCard(MessageUI(content = "jghvchjcvbnbvnbvjmhjmjkhgfkhjgkghkghkjhghghjhghjg,hjkghjkhlhjlhjghkjhl", timestamp = "02:38 AM"))
}