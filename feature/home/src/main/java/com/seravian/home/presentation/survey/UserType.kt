package com.seravian.home.presentation.survey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.setValue
import com.seravian.ui.theme.inversePrimaryDarkMediumContrast
import com.seravian.ui.theme.outlineVariantDarkMediumContrast


@Composable
fun GetUserType(modifier: Modifier = Modifier, navigateToGetUserData: () -> Unit) {

    var selectedOption by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) } // To track error state

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress Bar and Close Icon
        Text(
            text = "1 of 2",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = 0.0f,
            modifier = Modifier.fillMaxWidth(),
            trackColor = outlineVariantDarkMediumContrast,
            color = inversePrimaryDarkMediumContrast
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Title and Subtitle
        Text(
            text = "Are you a Doctor or a Patient?",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Select one.",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Options with Doctor and Patient
        val options = listOf(
            "Doctor" to com.seravian.ui.R.drawable.doctor_ic,
            "Patient" to com.seravian.ui.R.drawable.patient_ic
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { (label, iconRes) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selectedOption == label) Color(0xFFE0E0E0) else Color.White)
                        .selectable(
                            selected = selectedOption == label,
                            onClick = {
                                selectedOption = label
                                showError = false // Reset error when a valid option is selected
                            }
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = label,
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Error Message
        if (showError) {
            Text(
                text = "Please select an option to proceed.",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Next Button
        Button(
            onClick = {
                if (selectedOption.isEmpty()) {
                    showError = true
                } else {
                    navigateToGetUserData()
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Next")
        }
    }
}








@Preview(showSystemUi = true)
@Composable
fun GetUserTypePreview() {
        GetUserType(navigateToGetUserData = { })
}