package com.seravian.onboarding.presentation.screens

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenvenom.ui.theme.AppTheme
import com.greenvenom.validation.domain.ValidationError
import com.greenvenom.validation.domain.ValidationResult
import com.greenvenom.validation.util.toString
import com.greenvenom.validation.components.DatePickerField
import com.greenvenom.validation.components.PhoneNumberField

@Composable
fun OnBoardingDataContent(
    mobileNumberValidationResult: ValidationResult<String, ValidationError>?,
    fullNameValidationResult: ValidationResult<Unit, ValidationError>?,
    validateFullName: (String) -> Unit,
    validatePhoneNumber: (String, String) -> Unit,
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
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = fullNameValidationResult is ValidationResult.Error,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        if (fullNameValidationResult is ValidationResult.Error) {
            val error = fullNameValidationResult.error
            Text(text = error.toString(context), color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(22.dp))

        PhoneNumberField(
            isMobileNumberValid = fullNameValidationResult is ValidationResult.Success,
            retrievePhoneNumber = { mobileNumber, code ->
                validatePhoneNumber(mobileNumber, code)
            }
        )
        if (mobileNumberValidationResult is ValidationResult.Error) {
            val error = mobileNumberValidationResult.error
            Text(text = error.toString(context), color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(22.dp))

        DatePickerField(
            label = "Date of Birth",
            onDateSelected = { date ->
                birthDate = date
            }
        )

        Spacer(modifier = Modifier.height(90.dp))

        Button(
            onClick = { onSubmitClicked() },
            enabled = mobileNumberValidationResult is ValidationResult.Success &&
                    fullNameValidationResult is ValidationResult.Success &&
                    !birthDate.isNullOrBlank(),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        ) {
            Text(text = "Submit")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun OnBoardingDataContentPreview() {
    AppTheme {
        OnBoardingDataContent(
            mobileNumberValidationResult = ValidationResult.Success("Unit"),
            fullNameValidationResult = ValidationResult.Error(ValidationError.EMPTY_NAME),
            validateFullName = {},
            validatePhoneNumber = { _, _ -> },
            onSubmitClicked = {}
        )
    }
}