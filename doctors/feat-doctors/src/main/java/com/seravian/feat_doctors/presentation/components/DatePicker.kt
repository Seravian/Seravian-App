package com.seravian.feat_doctors.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // You can replace this with a calendar UI or Android DatePickerDialog
    Text("Date: ${selectedDate.toString()}")
}
