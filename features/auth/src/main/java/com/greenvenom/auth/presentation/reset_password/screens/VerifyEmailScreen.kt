package com.greenvenom.auth.presentation.reset_password.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.greenvenom.auth.R
import com.greenvenom.auth.component.AuthCustomButton
import com.greenvenom.auth.component.AuthHeader
import com.greenvenom.auth.component.AuthTextField
import com.greenvenom.auth.data.repository.EmailStateRepository
import com.greenvenom.auth.presentation.EmailState
import com.greenvenom.auth.presentation.reset_password.ResetPasswordAction
import com.greenvenom.auth.presentation.reset_password.ResetPasswordState
import com.greenvenom.auth.presentation.reset_password.ResetPasswordViewModel
import com.greenvenom.networking.data.onError
import com.greenvenom.networking.data.onSuccess
import com.greenvenom.networking.utils.toString
import com.greenvenom.ui.presentation.BaseAction
import com.greenvenom.ui.presentation.BaseScreen
import com.greenvenom.ui.theme.AppTheme
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString
import org.koin.compose.koinInject

@Composable
fun VerifyEmailScreen(
    navigateBack: () -> Unit,
    navigateToOtpScreen: () -> Unit
) {
    val emailStateRepository: EmailStateRepository = koinInject()
    val emailState by emailStateRepository.emailState.collectAsStateWithLifecycle()

    BaseScreen<ResetPasswordViewModel> { resetPasswordViewModel ->
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
            baseActions(BaseAction.ShowErrorMessage(
                it.errorType?.toString(context)?: context.getString(R.string.something_went_wrong)
            ))
            resetPasswordActions(ResetPasswordAction.ResetEmailResult)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    resetPasswordActions(ResetPasswordAction.UpdateEmail(email))
                },
                label = stringResource(R.string.enter_your_email),
                error = if (emailState.emailValidity is ValidationResult.Error) emailState.emailValidity.error.toString(context) else "",
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthCustomButton(
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