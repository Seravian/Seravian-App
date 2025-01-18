package com.seravian.onboarding.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.seravian.onboarding.R
import com.seravian.onboarding.presentation.components.UserTypeOptionCard
import com.seravian.ui.theme.SeravianTheme

@Composable
fun OnBoardingTypeContent(
    onOptionSelected: (String) -> Unit,
    navigateBack: () -> Unit = {},
    options: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { (label, iconRes) ->
                UserTypeOptionCard(
                    label = label,
                    iconRes = iconRes,
                    isSelected = label == selectedOption,
                    onOptionSelected = {
                        selectedOption = it
                        onOptionSelected(it)
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@Preview(showSystemUi = true)
@Composable
private fun OnBoardingTypeContentPreview() {
    SeravianTheme {
        OnBoardingTypeContent(
            onOptionSelected = { },
            options = listOf(
                "Doctor" to R.drawable.male_ic,
                "Patient" to R.drawable.female_ic
            )
        )
    }
}