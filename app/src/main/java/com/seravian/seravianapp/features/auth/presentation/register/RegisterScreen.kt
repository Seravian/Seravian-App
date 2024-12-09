package com.seravian.seravianapp.features.auth.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seravian.domain.auth.CredentialState
import com.seravian.seravianapp.R
import com.seravian.seravianapp.common.components.AuthCustomButton
import com.seravian.seravianapp.common.components.AuthTextField
import com.seravian.seravianapp.core.navigation.AppDestination
import com.seravian.seravianapp.core.navigation.AppNavigator
import com.seravian.seravianapp.core.presentation.BaseScreen
import com.seravian.seravianapp.features.auth.presentation.login.LoginAction
import com.seravian.seravianapp.ui.theme.AuthBlackColor
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.bluePrimary

@Composable
fun RegisterScreen(
    appNavigator: AppNavigator?,
    modifier: Modifier = Modifier
) {
    BaseScreen<RegisterViewModel> { viewModel ->
        val state = viewModel.state
        RegisterContents(
            appNavigator = appNavigator,
            state = state,
            action = viewModel::registerAction
        )
    }
}

@Composable
fun RegisterContents(
    appNavigator: AppNavigator?,
    state: RegisterInputState,
    action: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val userName by remember { mutableStateOf("") }
    val email by remember { mutableStateOf("") }
    val password by remember { mutableStateOf("") }
    val confirmPassword by remember { mutableStateOf("") }

    Column() {
        Box(
            modifier = Modifier
                .background(color = AuthBlackColor)
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Stars",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 20.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                IconButton(onClick = { appNavigator?.navigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.white_arrow_back_2),
                        contentDescription = "Back Button",
                        tint = colorResource(R.color.white),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 50.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp) // Adds spacing between items
        ) {
            // Username Field
            Text(stringResource(R.string.userName))
            AuthTextField(
                value = userName,
                onValueChange = {
                    action(RegisterAction.ValidateUsername(it))
                },
                label = stringResource(R.string.enter_your_username),
                error = if (state.usernameValidity is CredentialState.InValid) state.usernameValidity.message else "",
                isPasswordField = false
            )


            // Email Field
            Text(stringResource(R.string.email))
            AuthTextField(
                value = email,
                onValueChange = {
                    action(RegisterAction.ValidateEmail(it))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is CredentialState.InValid) state.emailValidity.message else "",
                isPasswordField = false
            )


            // Password Field
            Text(stringResource(R.string.Password))
            AuthTextField(
                value = password,
                onValueChange = {
                    action(RegisterAction.ValidatePassword(it))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is CredentialState.InValid) state.passwordValidity.message else "",
                isPasswordField = true
            )


            // Confirm Password Field
            Text(stringResource(R.string.confirmPassword))
            AuthTextField(
                value = confirmPassword,
                onValueChange = {
                    action(RegisterAction.ValidatePassword(it))
                },
                label = stringResource(R.string.confirmYourPassword),
                error = if (state.confirmPasswordValidity is CredentialState.InValid) state.confirmPasswordValidity.message else "",
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
                            confirmPassword
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
            appNavigator = null,
            state = mockState,
            action = { }
        )
    }
}