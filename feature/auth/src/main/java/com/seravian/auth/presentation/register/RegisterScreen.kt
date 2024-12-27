package com.seravian.auth.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seravian.auth.util.toString
import com.seravian.domain.network.Result
import com.seravian.domain.network.onSuccess
import com.seravian.auth.R
import com.seravian.auth.component.AuthHeader
import com.seravian.ui.components.AuthCustomButton
import com.seravian.ui.components.AuthTextField
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.backgroundLight
import com.seravian.ui.theme.onBackgroundLight

@Composable
fun RegisterScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        val state = viewModel.registerState.collectAsState()

        RegisterContent(
            state = state.value,
            action = viewModel::registerAction,
            navigateBack = navigateBack
        )
    }
}

@Composable
private fun RegisterContent(
    state: RegisterInputState,
    action: (RegisterAction) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    state.registrationResult?.onSuccess {
        action(RegisterAction.ResetRegisterState)
        navigateBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundLight)
    ) {
        // Header Section
        AuthHeader(
            title = stringResource(R.string.register),
            isLoginScreen = false,
            navigateBack = navigateBack
        )
        // Input Fields Section
        Column(
            verticalArrangement = Arrangement.spacedBy(13.dp), // Adds spacing between items
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Username Field
            Text(
                text = stringResource(R.string.userName),
                color = onBackgroundLight
            )
            AuthTextField(
                value = userName,
                onValueChange = {
                    userName = it
                    action(RegisterAction.ValidateUsername(userName))
                },
                label = stringResource(R.string.enter_your_username),
                error = if (state.usernameValidity is Result.Error) state.usernameValidity.error.toString(context) else "",
                isPasswordField = false
            )
            // Email Field
            Text(
                text = stringResource(R.string.email),
                color = onBackgroundLight
            )
            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    action(RegisterAction.ValidateEmail(email))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is Result.Error) state.emailValidity.error.toString(context) else "",
                isPasswordField = false
            )
            // Password Field
            Text(
                text = stringResource(R.string.Password),
                color = onBackgroundLight
            )
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    action(RegisterAction.ValidatePassword(password))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is Result.Error) state.passwordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            // Confirm Password Field
            Text(
                text = stringResource(R.string.confirmPassword),
                color = onBackgroundLight
                )
            AuthTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    action(RegisterAction.ValidatePasswordConfirmation(password, confirmPassword))
                },
                label = stringResource(R.string.confirmYourPassword),
                error = if (state.confirmPasswordValidity is Result.Error) state.confirmPasswordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            // Register Button
            AuthCustomButton(
                text = stringResource(R.string.register),
                onClick = {
                    action(
                        RegisterAction.Register(
                            userName,
                            email,
                            password,
                        )
                    )
                }
            )
        }
    }
}

val mockState = RegisterInputState(
    usernameValidity = null,
    emailValidity = null,
    passwordValidity = null,
    confirmPasswordValidity = null
)

@Preview(showSystemUi = true)
@Composable
private fun RegisterContentsPreview() {
    SeravianTheme {
        RegisterContent(
            state = mockState,
            action = { },
            navigateBack = { }
        )
    }
}