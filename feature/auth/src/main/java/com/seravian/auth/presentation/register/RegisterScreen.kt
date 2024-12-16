package com.seravian.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.auth.util.toString
import com.seravian.domain.network.util.Result
import com.seravian.domain.network.util.onSuccess
import com.seravian.auth.R
import com.seravian.ui.components.AuthCustomButton
import com.seravian.ui.components.AuthTextField
import com.seravian.ui.presentation.BaseScreen
import com.seravian.ui.theme.SeravianTheme
import com.seravian.ui.theme.backgroundLight
import com.seravian.ui.theme.onBackgroundLight

@Composable
fun RegisterScreen(
    navigateBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        val state = viewModel.registerState.collectAsState()

        RegisterContents(
            state = state.value,
            action = viewModel::registerAction,
            navigateBackToLogin = navigateBackToLogin
        )
    }
}

@Composable
fun RegisterContents(
    state: RegisterInputState,
    action: (RegisterAction) -> Unit,
    navigateBackToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var userName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    state.registrationResult?.onSuccess {
        action(RegisterAction.ResetRegisterState)
        navigateBackToLogin()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundLight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(id = R.drawable.stars),
                contentDescription = "Stars",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                IconButton(onClick = { navigateBackToLogin() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.white_arrow_back),
                        contentDescription = "Back Button",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = stringResource(R.string.register),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 50.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp)
                )
            }
        }
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
        RegisterContents(
            state = mockState,
            action = { },
            navigateBackToLogin = { }
        )
    }
}