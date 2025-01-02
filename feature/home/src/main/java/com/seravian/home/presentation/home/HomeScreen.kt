package com.seravian.home.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.home.presentation.components.DoctorCard
import com.seravian.home.presentation.components.FactCard
import com.seravian.ui.R
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    BaseScreen<HomeViewModel> { viewModel ->
        HomeContent()
    }
}

@Composable
private fun HomeContent(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Sliding Images Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
            modifier = Modifier.padding(vertical = 8.dp)
        )
        LazyRow {
            items(10) { // Placeholder for doctor items
                DoctorCard(
                    name = "Dr. John Doe",
                    title = "Cardiologist",
                    location = "New York, USA",
                    image = R.drawable.logo,
                    navigateToDetails = {}
                )
            }
        }

        // Small Facts Section
        Text(
            text = "Small Facts",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        repeat(5) { // Placeholder for fact cards
            FactCard(
                fact = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
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