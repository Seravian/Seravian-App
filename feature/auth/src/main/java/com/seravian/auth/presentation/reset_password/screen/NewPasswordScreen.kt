package com.seravian.auth.presentation.reset_password.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.auth.R
import com.seravian.auth.component.AuthHeader
import com.seravian.auth.presentation.reset_password.ResetPasswordAction
import com.seravian.auth.presentation.reset_password.ResetPasswordState
import com.seravian.auth.presentation.reset_password.ResetPasswordViewModel
import com.seravian.auth.util.toString
import com.seravian.domain.network.Result
import com.seravian.auth.component.AuthCustomButton
import com.seravian.auth.component.AuthTextField
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.backgroundLight
import com.seravian.ui.theme.onBackgroundLight

@Composable
fun NewPasswordScreen(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    BaseScreen<ResetPasswordViewModel> { viewModel ->
        val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle()

        NewPasswordContent(
            state = resetPasswordState,
            action = viewModel::resetPasswordAction,
            navigateToLoginScreen = navigateToLoginScreen,
            navigateBack = navigateBack
        )
    }
}

@Composable
private fun NewPasswordContent(
    state: ResetPasswordState,
    action: (ResetPasswordAction) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundLight)
    ) {
        AuthHeader(
            title = stringResource(R.string.create_new_password),
            navigateBack = navigateBack,
            isLoginScreen = false
        )
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(13.dp) // Adds spacing between items
        ) {
            Text(
                text = stringResource(R.string.Password),
                color = onBackgroundLight
            )
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    action(ResetPasswordAction.ValidatePassword(password))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is Result.Error) state.passwordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            // Confirm Password Field
            Text(
                text = stringResource(R.string.confirm_password),
                color = onBackgroundLight
            )
            AuthTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    action(ResetPasswordAction.ValidatePasswordConfirmation(password, confirmPassword))
                },
                label = stringResource(R.string.confirm_your_password),
                error = if (state.confirmPasswordValidity is Result.Error) state.confirmPasswordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomButton(
                text = stringResource(R.string.next),
                enabled = state.passwordValidity is Result.Success && state.confirmPasswordValidity is Result.Success,
                onClick = {
                    navigateToLoginScreen()
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewPasswordContentPreview() {
    SeravianTheme {
        NewPasswordContent(
            state = ResetPasswordState(),
            action = { },
            navigateToLoginScreen = { },
            navigateBack = { }
        )
    }
}