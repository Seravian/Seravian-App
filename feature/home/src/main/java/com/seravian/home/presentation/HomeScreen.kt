package com.seravian.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    BaseScreen<HomeViewModel> { viewModel ->
        HomeContent()
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .padding()
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Sliding Images Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(colorScheme.primaryContainer, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "Sliding Images",
                color = colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Famous Doctors Section
        Text(
            text = "Famous Doctors",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(10) { // Placeholder for doctor items
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(colorScheme.primaryContainer, CircleShape)
                    ) {
                        Text(
                            text = "Image",
                            color = colorScheme.onPrimaryContainer,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Text(
                        text = "Doctor Name",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Specialization",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorScheme.onBackground
                    )
                }
            }
        }

        // Small Facts Section
        Text(
            text = "Small Facts",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(100.dp)
                .background(colorScheme.secondaryContainer, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "Fact 1",
                color = colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    SeravianTheme {
        HomeContent()
    }
}