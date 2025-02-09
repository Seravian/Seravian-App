package com.greenvenom.auth.presentation.reset_password.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.greenvenom.auth.R
import com.greenvenom.auth.component.AuthCustomButton
import com.greenvenom.auth.component.AuthHeader
import com.greenvenom.auth.component.AuthTextField
import com.greenvenom.auth.presentation.reset_password.ResetPasswordAction
import com.greenvenom.auth.presentation.reset_password.ResetPasswordState
import com.greenvenom.auth.presentation.reset_password.ResetPasswordViewModel
import com.greenvenom.networking.data.onError
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.networking.utils.toString
import com.greenvenom.ui.presentation.BaseAction
import com.greenvenom.ui.presentation.BaseScreen
import com.greenvenom.ui.theme.AppTheme
import com.greenvenom.ui.theme.onBackgroundLight
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString

@Composable
fun NewPasswordScreen(
    navigateBack: () -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    BaseScreen<ResetPasswordViewModel> { viewModel ->
        val resetPasswordState by viewModel.resetPasswordState.collectAsStateWithLifecycle()

        NewPasswordContent(
            state = resetPasswordState,
            resetPasswordActions = viewModel::resetPasswordAction,
            baseActions = viewModel::baseAction,
            navigateToLoginScreen = navigateToLoginScreen,
            navigateBack = navigateBack
        )
    }
}

@Composable
private fun NewPasswordContent(
    state: ResetPasswordState,
    resetPasswordActions: (ResetPasswordAction) -> Unit,
    baseActions: (BaseAction) -> Unit,
    navigateToLoginScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.passwordUpdatedResult) {
        baseActions(BaseAction.HideLoading)
        state.passwordUpdatedResult?.onSuccess {
            navigateToLoginScreen()
        }
        state.passwordUpdatedResult?.onError {
            baseActions(BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: it.message.ifEmpty { "Something went wrong" }
            ))
            resetPasswordActions(ResetPasswordAction.ResetPasswordResult)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            resetPasswordActions(ResetPasswordAction.ResetState)
            password = ""
            confirmPassword = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AuthHeader(
            title = stringResource(R.string.create_new_password),
            navigateBack = navigateBack,
            isLoginScreen = false,
            isNavigationBackWanted = true
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
                color = MaterialTheme.colorScheme.onBackground
            )
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    resetPasswordActions(ResetPasswordAction.ValidatePassword(password))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is ValidationResult.Error) state.passwordValidity.error.toString(context) else "",
                isPasswordField = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            // Confirm Password Field
            Text(
                text = stringResource(R.string.confirm_password),
                color = MaterialTheme.colorScheme.onBackground
            )
            AuthTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    resetPasswordActions(ResetPasswordAction.ValidatePasswordConfirmation(password, confirmPassword))
                },
                label = stringResource(R.string.confirm_your_password),
                error = if (state.confirmPasswordValidity is ValidationResult.Error) state.confirmPasswordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomButton(
                text = stringResource(R.string.confirm),
                enabled = state.passwordValidity is ValidationResult.Success && state.confirmPasswordValidity is ValidationResult.Success,
                onClick = {
                    baseActions(BaseAction.ShowLoading)
                    resetPasswordActions(ResetPasswordAction.UpdatePassword(password))
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewPasswordContentPreview() {
    AppTheme {
        NewPasswordContent(
            state = ResetPasswordState(),
            resetPasswordActions = { },
            baseActions = { },
            navigateToLoginScreen = { },
            navigateBack = { }
        )
    }
}