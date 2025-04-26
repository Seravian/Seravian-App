package com.greenvenom.validation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.greenvenom.validation.R

@Composable
fun PhoneNumberField(
    isMobileNumberValid: Boolean = true,
    retrievePhoneNumber: (mobileNumber: String, code: String) -> Unit,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    modifier: Modifier = Modifier
) {
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }
    var mobileNumber by rememberSaveable { mutableStateOf("") }

    CountryPickerOutlinedTextField(
        label = { Text(stringResource(R.string.mobile_number)) },
        defaultCountryCode = "EG",
        mobileNumber = mobileNumber,
        onMobileNumberChange = {
            mobileNumber = it
            retrievePhoneNumber(it, selectedCountry?.countryCode ?: "EG")
        },
        onCountrySelected = { selectedCountry = it },
        singleLine = true,
        isError = !isMobileNumberValid,
        shape = shape,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun PhoneNumberFieldPreview() {
    PhoneNumberField(
        isMobileNumberValid = true,
        retrievePhoneNumber = { _, _ -> },
    )
}