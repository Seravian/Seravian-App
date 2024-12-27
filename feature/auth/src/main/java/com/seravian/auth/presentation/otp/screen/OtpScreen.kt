package com.seravian.auth.presentation.otp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.auth.presentation.otp.OtpAction
import com.seravian.auth.presentation.otp.OtpState
import com.seravian.auth.presentation.otp.component.OtpInputField
import com.seravian.auth.presentation.otp.viewmodel.OtpViewModel
import com.seravian.domain.network.onSuccess
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit
    ) {
    BaseScreen<OtpViewModel> { viewModel ->
        val otpState = viewModel.otpState.collectAsStateWithLifecycle().value
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
            navigate = navigate,
            modifier = modifier
        )
    }
}

@Composable
private fun OtpContent(
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    action: (OtpAction) -> Unit,
    navigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    state.otpResult?.onSuccess {
        action(OtpAction.OnSuccess)
        navigate()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
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
            navigate = {}
        )
    }
}