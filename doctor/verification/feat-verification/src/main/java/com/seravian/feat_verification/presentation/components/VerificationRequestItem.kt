package com.seravian.feat_verification.presentation.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.core_verification.domain.utils.VerificationStatus
import com.seravian.feat_verification.R
import com.seravian.feat_verification.presentation.models.VerificationUI

@Composable
fun VerificationRequestItem(
    request: VerificationUI,
    onClick: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.verification_request, request.id),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = request.requestedAtUtc,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = request.status.normalized,
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (request.status) {
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

@Preview
@Composable
private fun ItemPreview() {
    AppTheme {
        VerificationRequestItem(
            request = VerificationUI(
                status = VerificationStatus.APPROVED,
                requestedAtUtc = "10 June, 2025"
            ),
            onClick = {}
        )
    }
}