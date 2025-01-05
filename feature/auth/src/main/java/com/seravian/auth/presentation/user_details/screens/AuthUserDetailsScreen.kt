package com.seravian.auth.presentation.user_details.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.auth.R
import com.seravian.auth.presentation.user_details.AuthUserDetailsAction
import com.seravian.auth.presentation.user_details.AuthUserDetailsState
import com.seravian.auth.presentation.user_details.AuthUserDetailsViewModel
import com.seravian.auth.presentation.user_details.components.SectionedProgressIndicator
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme

@Composable
fun AuthUserDetailsScreen(
    navigateToHomeScreen: () -> Unit
) {
    BaseScreen<AuthUserDetailsViewModel> { viewModel ->
        val state by viewModel.userDetailsState.collectAsStateWithLifecycle()

        AuthUserDetailsContent(
            state = state,
            action = viewModel::userDetailsAction,
            navigateToHomeScreen = navigateToHomeScreen
        )
    }
}

@Composable
private fun AuthUserDetailsContent(
    state: AuthUserDetailsState,
    action: (AuthUserDetailsAction) -> Unit,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        SectionedProgressIndicator(
            currentStep = state.currentStep,
            totalSteps = 3,
            title = when (state.currentStep) {
                1 -> "Choose Account Type"
                2 -> "Choose Your Gender"
                else -> "Enter Your Details"
            },
        )

        when (state.currentStep) {
            1 -> AuthUserTypeContent(
                onOptionSelected = {
                    action(AuthUserDetailsAction.UpdateAuthUserType(it))
                    action(AuthUserDetailsAction.NavigateForm(true))
                },
                options = listOf(
                    "Doctor" to R.drawable.doctor_ic,
                    "Patient" to R.drawable.patient_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )
            2 -> AuthUserTypeContent(
                onOptionSelected = {
                    action(AuthUserDetailsAction.UpdateAuthUserGender(it))
                    action(AuthUserDetailsAction.NavigateForm(true))
                },
                navigateBack = { action(AuthUserDetailsAction.NavigateForm(false)) },
                options = listOf(
                    "Male" to R.drawable.male_ic,
                    "Female" to R.drawable.female_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )
            else -> UserDataContent(
                navigateToHome = navigateToHomeScreen
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ContentPreview() {
    SeravianTheme {
        AuthUserDetailsContent(
            state = AuthUserDetailsState(currentStep = 3),
            action = { },
            navigateToHomeScreen = { }
        )
    }
}