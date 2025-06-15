package com.seravian.feat_home.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.core_ui.components.TopAppBar
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.seravian.feat_home.R
import com.seravian.feat_home.presentation.models.toUI
import com.seravian.feat_home.presentation.viewmodel.HomeState
import com.seravian.feat_home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen() {
    BaseScreen<HomeViewModel>(
        enableCustomBack = false
    ) { viewModel ->
        val state by viewModel.homeState.collectAsStateWithLifecycle()

        HomeContent(
            homeState = state
        )
    }
}

@Composable
private fun HomeContent(
    homeState: HomeState
) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTab by remember { mutableIntStateOf(0) }

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
            start = 8.dp,
            end = 8.dp
        )
        Column(
            modifier = Modifier
                .padding(modifiedPadding)
                .fillMaxSize()
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorScheme.primary.copy(alpha = 0.1f),
                                colorScheme.secondary.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_support),
                        contentDescription = stringResource(R.string.support),
                        tint = colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.support_center),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle Switch Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = {
                            Text(
                                text = stringResource(R.string.mental_health_advice),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_psychology),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilterChip(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = {
                            Text(
                                text = stringResource(R.string.qa_section),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_question_answer),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content Section
            when (selectedTab) {
                0 -> MentalHealthAdviceSection(
                    advices = homeState.disordersAdvices.map { it.toUI() },
                    modifier = Modifier
                        .weight(1f)
                )
                1 -> QuestionAnswerSection(
                    questionAnswers = homeState.questionAnswers.map { it.toUI() },
                    modifier = Modifier
                        .weight(1f)
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
        HomeContent(
            homeState = HomeState()
        )
    }
}