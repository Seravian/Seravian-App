package com.seravian.home.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.seravian.home.util.DatePicker
import com.seravian.ui.theme.inversePrimaryDarkMediumContrast
import com.seravian.ui.theme.outlineVariantDarkMediumContrast

@Composable
fun GetUserData(modifier: Modifier = Modifier, navigateToHome: () -> Unit) {

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") } // Default gender selection
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "2 of 2",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = 0.5f, // 50% progress
            modifier = Modifier.fillMaxWidth(),
            trackColor = outlineVariantDarkMediumContrast,
            color = inversePrimaryDarkMediumContrast
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Enter Your Details", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = fullName,
            onValueChange = {
                fullName = it
                showError = false
            },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isFullNameValid(fullName)
        )
        if (!isFullNameValid(fullName)) {
            Text(
                text = "Full Name must contain only letters",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Gender selection
        Text("Select Gender", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.RadioButton(
                    selected = gender == "Male",
                    onClick = { gender = "Male" }
                )
                Text("Male", style = MaterialTheme.typography.bodyMedium)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material3.RadioButton(
                    selected = gender == "Female",
                    onClick = { gender = "Female" }
                )
                Text("Female", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it.filter { char -> char.isDigit() } // Allow only digits
                showError = false
            },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number), // Numeric keyboard
            modifier = Modifier.fillMaxWidth(),
            isError = !isPhoneNumberValid(phoneNumber)
        )
        if (!isPhoneNumberValid(phoneNumber)) {
            Text(
                text = "Phone Number must be 11 digits\nand start with 012, 011, 010, or 015",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Using the DatePicker
        DatePicker(
            label = "Select your birth date",
            onDateSelected = { date ->
                birthDate = date
                showError = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected Date: $birthDate", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.weight(1f))

        if (showError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                when {
                    fullName.isBlank() || !isFullNameValid(fullName) -> {
                        showError = true
                        errorMessage = "Please enter a valid Full Name."
                    }
                    phoneNumber.isBlank() || !isPhoneNumberValid(phoneNumber) -> {
                        showError = true
                        errorMessage = "Please enter a valid Phone Number."
                    }
                    birthDate.isBlank() -> {
                        showError = true
                        errorMessage = "Please select a birth date."
                    }
                    else -> navigateToHome()
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Submit")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun GetUserDataPreview() {
    GetUserData(navigateToHome = {})
}



fun isPhoneNumberValid(number: String): Boolean {
    val validPrefixes = listOf("012", "011", "010", "015")
    return number.length == 11 && number.all { it.isDigit() } && validPrefixes.any { number.startsWith(it) }
}

fun isFullNameValid(name: String): Boolean = name.all { it.isLetter() || it.isWhitespace() }
