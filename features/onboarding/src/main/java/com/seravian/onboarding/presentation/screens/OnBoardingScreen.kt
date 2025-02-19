package com.seravian.onboarding.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.networking.data.onError
import com.seravian.onboarding.R
import com.seravian.onboarding.presentation.OnBoardingAction
import com.seravian.onboarding.presentation.OnBoardingState
import com.seravian.onboarding.presentation.OnBoardingViewModel
import com.seravian.onboarding.presentation.components.SectionedProgressIndicator
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.networking.utils.toString
import com.greenvenom.ui.presentation.BaseAction
import com.greenvenom.ui.presentation.BaseScreen
import com.greenvenom.ui.theme.AppTheme

@Composable
fun OnBoardingScreen(
    navigateToNextScreen: () -> Unit
) {
    BaseScreen<OnBoardingViewModel> { viewModel ->
        val state by viewModel.userDetailsState.collectAsStateWithLifecycle()

        OnBoardingContent(
            state = state,
            detailsAction = viewModel::userDetailsAction,
            baseAction = viewModel::baseAction,
            navigateToNextScreen = navigateToNextScreen
        )
    }
}

@Composable
private fun OnBoardingContent(
    state: OnBoardingState,
    detailsAction: (OnBoardingAction) -> Unit,
    baseAction: (BaseAction) -> Unit,
    navigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(state.isDetailsFull) {
        detailsAction(OnBoardingAction.UploadDetailsAuth)
    }

    LaunchedEffect(state.uploadingDetailsResult) {
        baseAction(BaseAction.HideLoading)
        state.uploadingDetailsResult?.onSuccess {
            navigateToNextScreen()
        }
        state.uploadingDetailsResult?.onError {
            baseAction(BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: context.getString(R.string.something_went_wrong)
            ))
            detailsAction(OnBoardingAction.ResetNetworkResult)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            detailsAction(OnBoardingAction.ResetState)
        }
    }

    BackHandler(enabled = state.currentStep != 1) {
        detailsAction(OnBoardingAction.NavigateForm(false))
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
                1 -> stringResource(R.string.select_user_type)
                2 -> stringResource(R.string.select_gender)
                else -> stringResource(R.string.enter_details)
            },
        )

        when (state.currentStep) {
            1 -> OnBoardingTypeContent(
                onOptionSelected = {
                    detailsAction(OnBoardingAction.UpdateAuthUserType(it))
                    detailsAction(OnBoardingAction.NavigateForm(true))
                },
                options = listOf(
                    stringResource(R.string.doctor) to R.drawable.doctor_ic,
                    stringResource(R.string.patient) to R.drawable.patient_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )

            2 -> OnBoardingTypeContent(
                onOptionSelected = {
                    detailsAction(OnBoardingAction.UpdateAuthUserGender(it))
                    detailsAction(OnBoardingAction.NavigateForm(true))
                },
                navigateBack = { detailsAction(OnBoardingAction.NavigateForm(false)) },
                options = listOf(
                    stringResource(R.string.male) to R.drawable.male_ic,
                    stringResource(R.string.female) to R.drawable.female_ic
                ),
                modifier = Modifier.fillMaxHeight(0.8f)
            )

            3 -> OnBoardingDataContent(
                mobileNumberValidationResult = state.mobileNumberValidationResult,
                fullNameValidationResult = state.fullNameValidationResult,
                validateFullName = { detailsAction(OnBoardingAction.ValidateFullName(it)) },
                validatePhoneNumber = { mobileNumber, countryCode ->
                    detailsAction(OnBoardingAction.ValidatePhoneNumber(mobileNumber, countryCode))
                },
                onSubmitClicked = {
                    baseAction(BaseAction.ShowLoading)
                    detailsAction(
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
    AppTheme {
        OnBoardingContent(
            state = OnBoardingState(currentStep = 3),
            detailsAction = { },
            baseAction = { },
            navigateToNextScreen = { }
        )
    }
}