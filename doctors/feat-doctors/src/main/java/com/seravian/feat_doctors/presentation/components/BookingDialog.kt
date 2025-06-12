package com.seravian.feat_doctors.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenvenom.validation.components.DatePickerField
import com.greenvenom.validation.components.TimePickerField
import java.time.*
import java.time.format.DateTimeFormatter

@Composable
fun BookingDialog(
    doctorId: String,
    onDismiss: () -> Unit,
    onBook: (String, String) -> Unit
) {
    val today = remember { LocalDate.now() }
    var selectedDate by remember { mutableStateOf(today) }
    var fromTime by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var toTime by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                // Local DateTime
                val localFrom = LocalDateTime.of(selectedDate, fromTime)
                val localTo = LocalDateTime.of(selectedDate, toTime)

                // Check if from is before to
                if (localFrom.isAfter(localTo) || localFrom.isEqual(localTo)) {
                    showError = true
                    return@Button
                }

                // Convert to UTC
                val fromUtc = localFrom.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneOffset.UTC).toInstant()

                val toUtc = localTo.atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneOffset.UTC).toInstant()

                val formatter = DateTimeFormatter.ISO_INSTANT
                val fromUtcString = formatter.format(fromUtc)
                val toUtcString = formatter.format(toUtc)

                onBook(fromUtcString, toUtcString)
            }) {
                Text("Confirm Booking")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Book Appointment") },
        text = {
            Column {
                DatePickerField(
                    label = "Select Date",
                    onDateSelected = { dateString ->
                        selectedDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    }
                )

                Spacer(Modifier.height(8.dp))

                TimePickerField(
                    label = "From Time",
                    onTimeSelected = { timeString ->
                        fromTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
                    }
                )

                Spacer(Modifier.height(8.dp))

                TimePickerField(
                    label = "To Time",
                    onTimeSelected = { timeString ->
                        toTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"))
                    }
                )

                Spacer(Modifier.height(8.dp))

                if (showError) {
                    Text(
                        text = "'To Time' must be after 'From Time'",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }
        }
    )
}
