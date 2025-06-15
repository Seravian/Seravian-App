package com.seravian.feat_home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.seravian.feat_home.R
import com.seravian.feat_home.presentation.components.EmptyStateCard
import com.seravian.feat_home.presentation.components.MentalHealthAdviceCard
import com.seravian.feat_home.presentation.models.DisordersAdvicesUI

@Composable
fun MentalHealthAdviceSection(
    advices: List<DisordersAdvicesUI>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (advices.isEmpty()) {
            item {
                EmptyStateCard(
                    title = stringResource(R.string.no_advice_available),
                    subtitle = stringResource(R.string.advice_coming_soon),
                    icon = painterResource(R.drawable.ic_psychology)
                )
            }
        } else {
            items (advices) { advice ->
                MentalHealthAdviceCard(advice = advice)
            }
        }
    }
}