package com.seravian.feat_home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_home.presentation.components.DoctorCard
import com.seravian.feat_home.presentation.components.PreviousChatCard
import com.seravian.feat_home.R

@Composable
fun HomeScreen() {
    BaseScreen<HomeViewModel> { viewModel ->
        HomeContent()
    }
}

@Composable
private fun HomeContent() {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                isVisible = true,
                isSideDestination = false,
                isActionEnabled = false
            )
        }
    ) { innerPadding ->
        val modifiedPadding = PaddingValues(
            top = innerPadding.calculateTopPadding(),
            bottom = 0.dp,
            start = 16.dp,
            end = 16.dp
        )
        Column(
            modifier = Modifier
                .padding(modifiedPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            // Sliding Images Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(colorScheme.primaryContainer.copy(alpha = 0f))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.promoting_positive_emotions),
                    contentDescription = "Sliding Images",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Famous Doctors Section
            Text(
                text = stringResource(R.string.famous_doctors),
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow {
                items(10) { // Placeholder for doctor items
                    DoctorCard(
                        name = "Dr. John Doe",
                        title = "Cardiologist",
                        location = "New York, USA",
                        image = R.drawable.doctor_ic,
                        navigateToDetails = {}
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Previous Chats Section
            Text(
                text = stringResource(R.string.chat_history),
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            repeat(5) { // Placeholder for chats cards
                PreviousChatCard(
                    fact = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                )
            }
        }
    }
}

@PreviewLightDark
@Preview(showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    AppTheme {
        HomeContent()
    }
}