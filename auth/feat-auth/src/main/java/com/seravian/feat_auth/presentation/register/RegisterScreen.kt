package com.seravian.feat_auth.presentation.register

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.feat_auth.R
import com.seravian.core_ui.components.CustomButton
import com.seravian.feat_auth.presentation.component.AuthHeader
import com.seravian.core_ui.components.CustomTextField
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_network.utils.toString
import com.seravian.core_ui.presentation.BaseAction
import com.seravian.core_ui.presentation.BaseScreen
import com.seravian.core_ui.theme.AppTheme
import com.seravian.validation.domain.ValidationResult
import com.seravian.validation.util.toString
import androidx.core.net.toUri

@Composable
fun RegisterScreen(
    navigateBack: () -> Unit,
    navigateToAccountVerificationScreen: () -> Unit,
) {
    BaseScreen<RegisterViewModel>(
        onPhysicalBack = {
            navigateBack()
        }
    ) { viewModel ->
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
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var isPrivacyPolicyAccepted by rememberSaveable { mutableStateOf(false) }

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
            email = ""
            password = ""
            confirmPassword = ""
            isPrivacyPolicyAccepted = false
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
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

                Spacer(modifier = Modifier.height(4.dp))

                // Privacy Policy Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isPrivacyPolicyAccepted,
                        onCheckedChange = { isPrivacyPolicyAccepted = it }
                    )
                    Spacer(modifier = Modifier.width(2.dp))

                    val annotatedString = buildAnnotatedString {
                        append(stringResource(R.string.i_accept_the))

                        // Add clickable privacy policy link
                        pushStringAnnotation(
                            tag = "privacy_policy",
                            annotation = "privacy_policy_link"
                        )
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(stringResource(R.string.privacy_policy))
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedString,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "privacy_policy",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                val privacyPolicyUrl = "https://seravian.runasp.net/terms/Seravian%20Platform%20User%20Roles%20and%20Access%20Policy.pdf"

                                // Open privacy policy link
                                val intent = Intent(Intent.ACTION_VIEW, privacyPolicyUrl.toUri())
                                context.startActivity(intent)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                CustomButton(
                    text = stringResource(R.string.register),
                    onClick = {
                        registerActions(
                            RegisterAction.Register(
                                email,
                                password,
                            )
                        )
                        baseActions(BaseAction.ShowLoading)
                    },
                    enabled = state.emailValidity is ValidationResult.Success &&
                            state.passwordValidity is ValidationResult.Success &&
                            state.confirmPasswordValidity is ValidationResult.Success &&
                            isPrivacyPolicyAccepted
                )
            }
        }
    }
}

@Preview(showBackground = true)
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