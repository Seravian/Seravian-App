package com.seravian.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.seravian.ui.theme.bluePrimary
import com.seravian.ui.theme.onBackgroundLight

@Composable
fun LoginScreen(
    navigateToRegister: () -> Unit,
    navigateToEmailVerification: () -> Unit,
    navigateToHome: () -> Unit,
) {
    BaseScreen<LoginViewModel> { viewModel ->
        val state = viewModel.loginState.collectAsState()

        LoginContent(
            navigateToRegister = navigateToRegister,
            navigateToEmailVerification = navigateToEmailVerification,
            navigateToHome = navigateToHome,
            state = state.value,
            action = viewModel::loginAction,
        )
    }
}

@Composable
private fun LoginContent(
    state: LoginInputState,
    action: (LoginAction) -> Unit,
    navigateToRegister: () -> Unit,
    navigateToEmailVerification: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    state.loginResult?.onSuccess {
        action(LoginAction.ResetLoginState)
        navigateToHome()
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
            navigateToRegister = navigateToRegister
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
                        navigateToEmailVerification()
                    }
            )
            AuthCustomButton(
                text = stringResource(R.string.log_in),
                onClick = { action(LoginAction.Login(email, password)) }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    SeravianTheme {
        LoginContent(
            state = LoginInputState(),
            action = { },
            navigateToRegister = { },
            navigateToEmailVerification = { },
            navigateToHome = { }
        )
    }
}
