package com.seravian.feat_verification.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.seravian.core_verification.domain.utils.VerificationStatus

@Composable
fun VerificationRequestItem(
    title: String,
    status: VerificationStatus,
    date: String
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = status.normalized,
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (status) {
                        VerificationStatus.PENDING -> MaterialTheme.colorScheme.primary
                        VerificationStatus.APPROVED -> Color(0xFF4CAF50)
                        VerificationStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
                        VerificationStatus.DELETED -> MaterialTheme.colorScheme.errorContainer
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}