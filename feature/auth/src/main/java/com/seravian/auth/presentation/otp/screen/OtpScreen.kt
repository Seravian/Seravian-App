package com.seravian.auth.presentation.otp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.auth.R
import com.seravian.auth.component.AuthHeader
import com.seravian.auth.presentation.otp.OtpAction
import com.seravian.auth.presentation.otp.OtpState
import com.seravian.auth.presentation.otp.component.OtpInputField
import com.seravian.auth.presentation.otp.viewmodel.OtpViewModel
import com.seravian.domain.network.onSuccess
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.backgroundLight

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    navigateTo: () -> Unit,
    navigateBack: () -> Unit
    ) {
    BaseScreen<OtpViewModel> { viewModel ->
        val otpState by viewModel.otpState.collectAsStateWithLifecycle()
        val focusRequesters = remember {
            List(4) { FocusRequester() }
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
            focusRequesters = focusRequesters,
            action = { action ->
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
            navigateTo = navigateTo,
            navigateBack = navigateBack,
            modifier = modifier
        )
    }
}

@Composable
private fun OtpContent(
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    action: (OtpAction) -> Unit,
    navigateTo: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.otpResult) {
        state.otpResult?.onSuccess {
            action(OtpAction.OnSuccess)
            navigateTo()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundLight),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                .padding(16.dp)
        ) {
            state.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            action(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        action(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        action(OtpAction.OnKeyboardBack)
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
    SeravianTheme {
        OtpContent(
            state = OtpState(),
            focusRequesters = List(4) { FocusRequester() },
            action = {},
            navigateTo = {},
            navigateBack = {}
        )
    }
}