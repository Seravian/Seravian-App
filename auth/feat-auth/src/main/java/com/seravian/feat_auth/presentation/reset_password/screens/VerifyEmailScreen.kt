package com.seravian.feat_auth.presentation.reset_password.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seravian.feat_auth.R
import com.seravian.feat_auth.data.EmailState
import com.seravian.feat_auth.data.repository.EmailStateRepository
import com.seravian.core_ui.components.CustomButton
import com.seravian.feat_auth.presentation.component.AuthHeader
import com.seravian.core_ui.components.CustomTextField
import com.seravian.feat_auth.presentation.reset_password.ResetPasswordAction
import com.seravian.feat_auth.presentation.reset_password.ResetPasswordState
import com.seravian.feat_auth.presentation.reset_password.ResetPasswordViewModel
import com.seravian.core_network.data.onError
import com.seravian.core_network.data.onSuccess
import com.seravian.core_network.utils.toString
import com.seravian.core_ui.presentation.BaseAction
import com.seravian.core_ui.presentation.BaseScreen
import com.seravian.core_ui.theme.AppTheme
import com.seravian.validation.domain.ValidationError
import com.seravian.validation.domain.ValidationResult
import com.seravian.validation.util.toString
import org.koin.compose.koinInject

@Composable
fun VerifyEmailScreen(
    navigateBack: () -> Unit,
    navigateToOtpScreen: () -> Unit
) {
    val emailStateRepository = koinInject<EmailStateRepository>()
    val emailState by emailStateRepository.emailState.collectAsStateWithLifecycle()

    BaseScreen<ResetPasswordViewModel>(
        onPhysicalBack = {
            navigateBack()
        }
    ) { resetPasswordViewModel ->
        val resetPasswordState by resetPasswordViewModel.resetPasswordState.collectAsStateWithLifecycle()

        VerifyEmailContent(
            resetPasswordState = resetPasswordState,
            emailState = emailState,
            resetPasswordActions = resetPasswordViewModel::resetPasswordAction,
            baseActions = resetPasswordViewModel::baseAction,
            navigateToOtpScreen = navigateToOtpScreen,
            navigateBack = navigateBack
        )
    }
}

@Composable
private fun VerifyEmailContent(
    resetPasswordState: ResetPasswordState,
    emailState: EmailState,
    resetPasswordActions: (ResetPasswordAction) -> Unit,
    baseActions: (BaseAction) -> Unit,
    navigateToOtpScreen: () -> Unit,
    navigateBack: () -> Unit,
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(resetPasswordState.emailSentNetworkResult) {
        baseActions(BaseAction.HideLoading)
        resetPasswordState.emailSentNetworkResult?.onSuccess {
            navigateToOtpScreen()
        }
        resetPasswordState.emailSentNetworkResult?.onError {
            baseActions(
                BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: context.getString(R.string.something_went_wrong)
            ))
            resetPasswordActions(ResetPasswordAction.ResetEmailResult)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AuthHeader(
                title = stringResource(R.string.enter_your_email),
                navigateBack = navigateBack,
                isLoginScreen = false
            )
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(13.dp) // Adds spacing between items
            ) {
                Text(
                    text = stringResource(R.string.email),
                    color = MaterialTheme.colorScheme.onBackground
                )
                CustomTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        resetPasswordActions(ResetPasswordAction.UpdateEmail(email))
                    },
                    label = stringResource(R.string.enter_your_email),
                    error = if (emailState.emailValidity is ValidationResult.Error) {
                        (emailState.emailValidity as ValidationResult.Error<ValidationError>).error.toString(context)
                    } else "",
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomButton(
                    text = stringResource(R.string.next),
                    enabled = emailState.emailValidity is ValidationResult.Success,
                    onClick = {
                        baseActions(BaseAction.ShowLoading)
                        resetPasswordActions(ResetPasswordAction.SendResetPasswordEmail(email))
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun VerifyEmailContentPreview() {
    AppTheme {
        VerifyEmailContent(
            resetPasswordState = ResetPasswordState(),
            emailState = EmailState(),
            resetPasswordActions = { },
            baseActions = { },
            navigateToOtpScreen = { },
            navigateBack = { }
        )
    }
}