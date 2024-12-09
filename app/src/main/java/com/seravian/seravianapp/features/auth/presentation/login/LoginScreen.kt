package com.seravian.seravianapp.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
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
import com.seravian.seravianapp.ui.theme.AuthBlackColor
import com.seravian.seravianapp.ui.theme.SeravianTheme
import com.seravian.seravianapp.ui.theme.bluePrimary

@Composable
fun LoginScreen(
    appNavigator: AppNavigator?,
    modifier: Modifier = Modifier
) {
    BaseScreen<LoginViewModel> { viewModel ->
        LoginContents(
            appNavigator = appNavigator,
            state = viewModel.state,
            action = viewModel::loginAction
        )
    }
}

@Composable
fun LoginContents(
    appNavigator: AppNavigator?,
    state: LoginInputState,
    action: (LoginAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val email by remember { mutableStateOf("") }
    val password by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header Section
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
                Row {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.logo_image),
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = stringResource(R.string.seravian),
                        color = colorResource(R.color.white),
                        modifier = Modifier.padding(5.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.sign_in_into_your_account),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 30.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp)
                )

                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = stringResource(R.string.don_t_have_an_account),
                        color = colorResource(R.color.white),
                    )
                    Text(
                        text = stringResource(R.string.sign_up),
                        color = bluePrimary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .clickable(enabled = true) {
                                appNavigator?.navigateTo(AppDestination.Register)
                            }
                    )
                }
            }
        }
        // Input Fields Section
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp) // Adds spacing between items
        ) {
            //email field
            Text(stringResource(R.string.email))
            AuthTextField(
                value = email,
                onValueChange = {
                    action(LoginAction.ValidateEmail(it))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (state.emailValidity is CredentialState.InValid) state.emailValidity.message else "",
                isPasswordField = false
            )
            //password field
            Text(stringResource(R.string.Password))
            AuthTextField(
                value = password,
                onValueChange = {
                    action(LoginAction.ValidatePassword(it))
                },
                label = stringResource(R.string.enter_your_password),
                error = if (state.passwordValidity is CredentialState.InValid) state.passwordValidity.message else "",
                isPasswordField = true
            )
            //forgot field
            Text(
                stringResource(R.string.forgot_password),
                color = bluePrimary,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(enabled = true) {
                        appNavigator?.navigateTo(AppDestination.ForgotPassword)
                    }
            )
            AuthCustomButton(
                text = stringResource(R.string.log_in),
                onClick = {
                    action(LoginAction.Login(email, password))
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginContentsPreview() {
    SeravianTheme {
        LoginContents(
            appNavigator = null,
            state = LoginInputState(),
            action = { }
        )
    }
}
