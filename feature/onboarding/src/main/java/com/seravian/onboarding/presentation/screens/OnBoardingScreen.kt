package com.seravian.onboarding.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.onboarding.R
import com.seravian.onboarding.presentation.OnBoardingAction
import com.seravian.onboarding.presentation.OnBoardingState
import com.seravian.onboarding.presentation.OnBoardingViewModel
import com.seravian.onboarding.presentation.components.SectionedProgressIndicator
import com.greenvenom.networking.data.onSuccess
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme

@Composable
fun OnBoardingScreen(
    navigateToHomeScreen: () -> Unit
) {
    BaseScreen<OnBoardingViewModel> { viewModel ->
        val state by viewModel.userDetailsState.collectAsStateWithLifecycle()

        OnBoardingContent(
            state = state,
            action = viewModel::userDetailsAction,
            navigateToHomeScreen = navigateToHomeScreen
        )
    }
}

@Composable
private fun OnBoardingContent(
    state: OnBoardingState,
    action: (OnBoardingAction) -> Unit,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.isDetailsFull) {
        action(OnBoardingAction.UploadDetailsAuth)
        state.uploadingDetailsResult?.onSuccess {
            navigateToHomeScreen()
            action(OnBoardingAction.ResetState)
        }
    }

    BackHandler(enabled = state.currentStep != 1) {
        action(OnBoardingAction.NavigateForm(false))
    }

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
            1 -> OnBoardingTypeContent(
                onOptionSelected = {
                    action(OnBoardingAction.UpdateAuthUserType(it))
                    action(OnBoardingAction.NavigateForm(true))
                },
                options = listOf(
                    "Doctor" to R.drawable.doctor_ic,
                    "Patient" to R.drawable.patient_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )
            2 -> OnBoardingTypeContent(
                onOptionSelected = {
                    action(OnBoardingAction.UpdateAuthUserGender(it))
                    action(OnBoardingAction.NavigateForm(true))
                },
                navigateBack = { action(OnBoardingAction.NavigateForm(false)) },
                options = listOf(
                    "Male" to R.drawable.male_ic,
                    "Female" to R.drawable.female_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )
            else -> OnBoardingDataContent(
                mobileNumberValidationResult = state.mobileNumberValidationResult,
                fullNameValidationResult = state.fullNameValidationResult,
                validateFullName = {  action(OnBoardingAction.ValidateFullName(it)) },
                validatePhoneNumber = { mobileNumber, countryCode ->
                    action(OnBoardingAction.ValidatePhoneNumber(mobileNumber, countryCode))
                },
                onSubmitClicked = {
                    action(
                        OnBoardingAction.UpdateOnBoardingData(
                        fullName = state.userDetails.fullName ?: "",
                        birthDate = state.userDetails.birthDate ?: "",
                    ))
                },
                modifier = Modifier.fillMaxHeight(0.9f)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ContentPreview() {
    SeravianTheme {
        OnBoardingContent(
            state = OnBoardingState(currentStep = 3),
            action = { },
            navigateToHomeScreen = { }
        )
    }
}