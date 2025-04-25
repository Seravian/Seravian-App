package com.greenvenom.feat_onboarding.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.core_ui.components.CustomButton
import com.greenvenom.core_ui.theme.AppTheme
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString
import com.greenvenom.validation.components.DatePickerField
import com.greenvenom.validation.components.PhoneNumberField
import com.greenvenom.feat_onboarding.R

@Composable
fun OnBoardingDataContent(
    fullNameValidationResult: ValidationResult<Unit, ValidationError>?,
    validateFullName: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var fullName by rememberSaveable { mutableStateOf<String?>(null) }
    var birthDate by rememberSaveable { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            fullName = null
            birthDate = null
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        OutlinedTextField(
            value = fullName ?: "",
            onValueChange = {
                fullName = it
                validateFullName(it)
            },
            label = { Text(text = stringResource(R.string.full_name)) },
            modifier = Modifier.fillMaxWidth(),
            isError = fullNameValidationResult is ValidationResult.Error,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        if (fullNameValidationResult is ValidationResult.Error) {
            val error = fullNameValidationResult.error
            Text(
                text = error.toString(context),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        DatePickerField(
            label = stringResource(R.string.birth_date),
            onDateSelected = { date ->
                birthDate = date
            }
        )

        Spacer(modifier = Modifier.height(90.dp))

        CustomButton(
            text = stringResource(R.string.submit),
            onClick = { onSubmitClicked() },
            enabled = fullNameValidationResult is ValidationResult.Success &&
                    !birthDate.isNullOrBlank()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OnBoardingDataContentPreview() {
    AppTheme {
        OnBoardingDataContent(
            fullNameValidationResult = ValidationResult.Error(ValidationError.EMPTY_NAME),
            validateFullName = {},
            onSubmitClicked = {}
        )
    }
}