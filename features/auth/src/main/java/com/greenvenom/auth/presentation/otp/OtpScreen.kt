package com.greenvenom.auth.presentation.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.auth.R
import com.greenvenom.auth.component.AuthHeader
import com.greenvenom.auth.presentation.otp.components.OtpInputField
import com.greenvenom.networking.data.onError
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.networking.utils.toString
import com.greenvenom.ui.presentation.BaseAction
import com.greenvenom.ui.presentation.BaseScreen
import com.greenvenom.ui.theme.AppTheme

@Composable
fun OtpScreen(
    navigateToNewPasswordScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    BaseScreen<OtpViewModel> { viewModel ->
        val otpState by viewModel.otpState.collectAsStateWithLifecycle()
        val focusRequesters = remember {
            List(6) { FocusRequester() }
        }
        val focusManager = LocalFocusManager.current
        val keyboardManager = LocalSoftwareKeyboardController.current

        LaunchedEffect(otpState.focusedIndex) {
            otpState.focusedIndex?.let { index ->
                focusRequesters.getOrNull(index)?.requestFocus()
            }
        }

        LaunchedEffect(otpState.code, keyboardManager) {
            val allNumbersEntered = otpState.code.none { it == null }
            if(allNumbersEntered) {
                focusRequesters.forEach {
                    it.freeFocus()
                }
                focusManager.clearFocus()
                keyboardManager?.hide()
            }
        }

        OtpContent(
            state = otpState,
            otpActions = { action ->
                when(action) {
                    is OtpAction.OnEnterNumber -> {
                        if(action.number != null) {
                            focusRequesters[action.index].freeFocus()
                        }
                    }
                    else -> Unit
                }
                viewModel.otpAction(action)
            },
            baseActions = viewModel::baseAction,
            focusRequesters = focusRequesters,
            navigateToNewPasswordScreen = navigateToNewPasswordScreen,
            navigateBack = navigateBack,
        )
    }
}

@Composable
private fun OtpContent(
    state: OtpState,
    otpActions: (OtpAction) -> Unit,
    baseActions: (BaseAction) -> Unit,
    focusRequesters: List<FocusRequester>,
    navigateToNewPasswordScreen: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(state.otpResult) {
        baseActions(BaseAction.HideLoading)
        state.otpResult?.onSuccess {
            navigateToNewPasswordScreen()
        }
        state.otpResult?.onError {
            baseActions(BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: it.message.ifEmpty { "Something went wrong" }
            ))
            otpActions(OtpAction.ResetNetworkResult)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            otpActions(OtpAction.ResetState)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        AuthHeader(
            title = stringResource(R.string.enter_the_sent_otp),
            isLoginScreen = false,
            navigateBack = navigateBack,
            modifier = Modifier
                .align(Alignment.Start)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            otpActions(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        otpActions(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        otpActions(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OtpScreenPreview() {
    AppTheme {
        OtpContent(
            state = OtpState(),
            otpActions = {},
            baseActions = {},
            focusRequesters = List(6) { FocusRequester() },
            navigateToNewPasswordScreen = {},
            navigateBack = {}
        )
    }
}