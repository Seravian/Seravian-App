package com.seravian.auth.presentation.user_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.inversePrimaryDark
import com.seravian.ui.theme.inversePrimaryDarkHighContrast
import com.seravian.ui.theme.inversePrimaryDarkMediumContrast
import com.seravian.ui.theme.inversePrimaryLight
import com.seravian.ui.theme.inverseSurfaceDark
import com.seravian.ui.theme.inverseSurfaceLight

@Composable
fun SectionedProgressIndicator(
    title: String,
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = "Step $currentStep of $totalSteps",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(totalSteps) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (index <= currentStep - 1) inversePrimaryDarkMediumContrast
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@PreviewLightDark
@Composable
private fun BarPreview() {
    SeravianTheme {
        SectionedProgressIndicator(
            title = "Step 1 of 6",
            currentStep = 1,
            totalSteps = 6
        )
    }
}