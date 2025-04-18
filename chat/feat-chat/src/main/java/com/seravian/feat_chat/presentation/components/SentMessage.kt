package com.seravian.feat_chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.feat_chat.R
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
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.weight(1f, false)
        ) {
            Text(
                text = message.content,
                modifier = Modifier
                    .padding(12.dp)
                    .padding(end = 4.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SentMessageCardPreview() {
    SentMessageCard(MessageUI(content = "Helgdfggsfdgdfguhgfdgjgjhdfdfsgbldfgkidfgdfhgfdhgfdshhfglo", dateTime = "02:38 AM"))
}