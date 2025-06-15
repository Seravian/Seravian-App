package com.seravian.feat_doctors.presentation.components

import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.widget.TimePicker
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.util.*

@Composable
fun TimePickerField(
    label: String,
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTime by rememberSaveable { mutableStateOf<Calendar?>(null) }
    var showModal by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    // Show TimePickerDialog when modal is triggered
    if (showModal) {
        val calendar = selectedTime ?: Calendar.getInstance()
        TimePickerDialog(
            context,
            { _: TimePicker, hour: Int, minute: Int ->
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                selectedTime = cal
                onTimeSelected(convertTimeToString(cal))
                showModal = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    OutlinedTextField(
        value = selectedTime?.let { convertTimeToString(it) } ?: "",
        onValueChange = {},
        label = { Text(text = label) },
        placeholder = { Text("") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select time")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedTime) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (up != null) {
                        showModal = true
                    }
                }
            }
    )
}

fun convertTimeToString(calendar: Calendar): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(calendar.time)
}

@Preview(showBackground = true)
@Composable
private fun TimePickerPreview() {
    TimePickerField(
        label = "Choose Time",
        onTimeSelected = {}
    )
}
