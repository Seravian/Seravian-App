package com.greenvenom.feat_auth.presentation.register

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
import com.greenvenom.feat_auth.R
import com.greenvenom.core_ui.components.CustomButton
import com.greenvenom.feat_auth.presentation.component.AuthHeader
import com.greenvenom.core_ui.components.CustomTextField
import com.greenvenom.core_network.data.onError
import com.greenvenom.core_network.data.onSuccess
import com.greenvenom.core_network.utils.toString
import com.greenvenom.core_ui.presentation.BaseAction
import com.greenvenom.core_ui.presentation.BaseScreen
import com.greenvenom.core_ui.theme.AppTheme
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString

@Composable
fun RegisterScreen(
    navigateBack: () -> Unit,
    navigateToAccountVerificationScreen: () -> Unit,
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        val state by viewModel.registerState.collectAsStateWithLifecycle()

        RegisterContent(
            state = state,
            registerActions = viewModel::registerAction,
            baseActions = viewModel::baseAction,
            navigateBack = navigateBack,
            navigateToAccountVerificationScreen = navigateToAccountVerificationScreen
        )
    }
}

@Composable
private fun RegisterContent(
    state: RegisterState,
    registerActions: (RegisterAction) -> Unit,
    baseActions: (BaseAction) -> Unit,
    navigateBack: () -> Unit,
    navigateToAccountVerificationScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.registrationNetworkResult) {
        baseActions(BaseAction.HideLoading)
        state.registrationNetworkResult?.onSuccess {
            navigateToAccountVerificationScreen()
        }
        state.registrationNetworkResult?.onError {
            baseActions(
                BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: context.getString(R.string.something_went_wrong)
            ))
            registerActions(RegisterAction.ResetNetworkResult)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            registerActions(RegisterAction.ResetState)
            username = ""
            email = ""
            password = ""
            confirmPassword = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header Section
        AuthHeader(
            title = stringResource(R.string.register),
            isLoginScreen = false,
            navigateBack = navigateBack
        )
        // Input Fields Section
        Column(
            verticalArrangement = Arrangement.spacedBy(13.dp),
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Username Field
            Text(
                text = stringResource(R.string.user_name),
                color = MaterialTheme.colorScheme.onBackground
            )
            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    registerActions(RegisterAction.ValidateUsername(username))
                },
                label = stringResource(R.string.enter_your_username),
                error = if (state.usernameValidity is ValidationResult.Error) state.usernameValidity.error.toString(context) else "",
                isPasswordField = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            // Email Field
            Text(
                text = stringResource(R.string.email),
                color = MaterialTheme.colorScheme.onBackground
            )
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    registerActions(RegisterAction.ValidateEmail(email))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is ValidationResult.Error) state.emailValidity.error.toString(context) else "",
                isPasswordField = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            // Password Field
            Text(
                text = stringResource(R.string.Password),
                color = MaterialTheme.colorScheme.onBackground
            )
            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    registerActions(RegisterAction.ValidatePassword(password))
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
            CustomTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    registerActions(RegisterAction.ValidatePasswordConfirmation(password, confirmPassword))
                },
                label = stringResource(R.string.confirm_your_password),
                error = if (state.confirmPasswordValidity is ValidationResult.Error) state.confirmPasswordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Register Button
            CustomButton(
                text = stringResource(R.string.register),
                onClick = {
                    registerActions(
                        RegisterAction.Register(
                            username,
                            email,
                            password,
                        )
                    )
                    baseActions(BaseAction.ShowLoading)
                },
                enabled = state.usernameValidity is ValidationResult.Success &&
                        state.emailValidity is ValidationResult.Success &&
                        state.passwordValidity is ValidationResult.Success &&
                        state.confirmPasswordValidity is ValidationResult.Success
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RegisterContentsPreview() {
    AppTheme {
        RegisterContent(
            state = RegisterState(),
            registerActions = { },
            baseActions = { },
            navigateBack = { },
            navigateToAccountVerificationScreen = { }
        )
    }
}