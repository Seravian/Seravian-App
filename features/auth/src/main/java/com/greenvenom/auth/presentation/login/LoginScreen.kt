package com.greenvenom.auth.presentation.login

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
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
import com.greenvenom.networking.data.onError
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.networking.utils.toString
import com.greenvenom.ui.presentation.BaseAction
import com.greenvenom.ui.presentation.BaseScreen
import com.greenvenom.ui.theme.AppTheme
import com.greenvenom.ui.theme.bluePrimary
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString

@Composable
fun LoginScreen(
    navigateToRegisterScreen: () -> Unit,
    navigateToEmailVerificationScreen: () -> Unit,
    navigateToNextScreen:() -> Unit
) {
    BaseScreen<LoginViewModel> { viewModel ->
        val state by viewModel.loginState.collectAsStateWithLifecycle()

        LoginContent(
            state = state,
            loginActions = viewModel::loginAction,
            baseActions = viewModel::baseAction,
            navigateToRegisterScreen = navigateToRegisterScreen,
            navigateToEmailVerificationScreen = navigateToEmailVerificationScreen,
            navigateToNextScreen = navigateToNextScreen
        )
    }
}

@Composable
private fun LoginContent(
    state: LoginState,
    loginActions: (LoginAction) -> Unit,
    baseActions: (BaseAction) -> Unit,
    navigateToRegisterScreen: () -> Unit,
    navigateToEmailVerificationScreen: () -> Unit,
    navigateToNextScreen: ()-> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.loginNetworkResult) {
        baseActions(BaseAction.HideLoading)
        state.loginNetworkResult?.onSuccess {
            navigateToNextScreen()
        }
        state.loginNetworkResult?.onError {
            baseActions(BaseAction.ShowErrorMessage(
                it.errorType?.toString(context) ?: context.getString(R.string.something_went_wrong)
            ))
            loginActions(LoginAction.ResetNetworkResult)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            loginActions(LoginAction.ResetState)
            email = ""
            password = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header Section
        AuthHeader(
            title = stringResource(R.string.sign_in_to_your_account),
            isLoginScreen = true,
            isNavigationBackWanted = false,
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
                color = MaterialTheme.colorScheme.onBackground
            )
            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    loginActions(LoginAction.ValidateEmail(email))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is ValidationResult.Error) state.emailValidity.error.toString(context) else "",
                isPasswordField = false,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            //password field
            Text(
                text = stringResource(R.string.Password),
                color = MaterialTheme.colorScheme.onBackground
            )
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginActions(LoginAction.ValidatePassword(password))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is ValidationResult.Error) state.passwordValidity.error.toString(context) else "",
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
                onClick = {
                    baseActions(BaseAction.ShowLoading)
                    loginActions(LoginAction.Login(email, password))
                },
                enabled = state.emailValidity is ValidationResult.Success && state.passwordValidity is ValidationResult.Success
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    AppTheme {
        LoginContent(
            state = LoginState(),
            loginActions = { },
            baseActions = { },
            navigateToRegisterScreen = { },
            navigateToEmailVerificationScreen = { },
            navigateToNextScreen = {  },
        )
    }
}
