package com.seravian.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.auth.util.toString
import com.seravian.domain.network.Result
import com.seravian.domain.network.onSuccess
import com.seravian.auth.R
import com.seravian.auth.component.AuthHeader
import com.seravian.auth.component.AuthCustomButton
import com.seravian.auth.component.AuthTextField
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.backgroundLight
import com.seravian.ui.theme.bluePrimary
import com.seravian.ui.theme.onBackgroundLight

@Composable
fun LoginScreen(
    navigateToRegisterScreen: () -> Unit,
    navigateToEmailVerificationScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
) {
    BaseScreen<LoginViewModel> { viewModel ->
        val state by viewModel.loginState.collectAsStateWithLifecycle()

        LoginContent(
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToEmailVerificationScreen = navigateToEmailVerificationScreen,
            navigateToHomeScreen = navigateToHomeScreen,
            state = state,
            action = viewModel::loginAction,
        )
    }
}

@Composable
private fun LoginContent(
    state: LoginState,
    action: (LoginAction) -> Unit,
    navigateToRegisterScreen: () -> Unit,
    navigateToEmailVerificationScreen: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.loginResult) {
        state.loginResult?.onSuccess {
            action(LoginAction.ResetState)
            navigateToHomeScreen()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundLight)
    ) {
        // Header Section
        AuthHeader(
            title = stringResource(R.string.sign_in_to_your_account),
            isLoginScreen = true,
            navigateToRegister = navigateToRegisterScreen
        )
        // Input Fields Section
        Column(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(13.dp) // Adds spacing between items
        ) {
            //email field
            Text(
                text = stringResource(R.string.email),
                color = onBackgroundLight
            )
            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    action(LoginAction.ValidateEmail(email))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is Result.Error) state.emailValidity.error.toString(context) else "",
                isPasswordField = false
            )
            //password field
            Text(
                text = stringResource(R.string.Password),
                color = onBackgroundLight
            )
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    action(LoginAction.ValidatePassword(password))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is Result.Error) state.passwordValidity.error.toString(context) else "",
                isPasswordField = true
            )
            //forgot field
            Text(
                stringResource(R.string.forgot_password),
                color = bluePrimary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(enabled = true) {
                        navigateToEmailVerificationScreen()
                    }
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomButton(
                text = stringResource(R.string.log_in),
                onClick = { action(LoginAction.Login(email, password)) },
                enabled = state.emailValidity is Result.Success && state.passwordValidity is Result.Success
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    SeravianTheme {
        LoginContent(
            state = LoginState(),
            action = { },
            navigateToRegisterScreen = { },
            navigateToEmailVerificationScreen = { },
            navigateToHomeScreen = { }
        )
    }
}
