package com.seravian.feat_verification.presentation.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.feat_verification.R
import com.seravian.feat_verification.presentation.utils.createFileFromUri
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVerificationRequestContent(
    onDismiss: () -> Unit,
    onSubmit: (VerificationRequestsAction.SendVerificationRequest) -> Unit
) {
    var selectedDoctorTitle by remember { mutableStateOf<DoctorTitle?>(null) }
    var description by remember { mutableStateOf("") }
    var sessionPrice by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf(listOf<FileAttachment?>()) }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val maxAttachments = 10

    // Initialize with 2 empty attachment slots
    LaunchedEffect(Unit) {
        attachments = List(2) { null }
    }

    // File picker launchers for each attachment slot
    val filePickerLaunchers = (0 until maxAttachments).map { index ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                try {
                    val file = createFileFromUri(context, uri)
                    if (file != null) {
                        val fileAttachment = FileAttachment(file)
                        if (fileAttachment.isSupported()) {
                            attachments = attachments.toMutableList().apply {
                                this[index] = fileAttachment
                            }
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.unsupported_file_type_please_select_pdf_png_or_jpeg_files_only),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_selecting_file, e.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.new_verification_request),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    painter = painterResource(R.drawable.close_ic),
                    contentDescription = stringResource(R.string.close)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Doctor Title Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedDoctorTitle?.title ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(stringResource(R.string.doctor_title)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DoctorTitle.entries.forEach { title ->
                    DropdownMenuItem(
                        text = { Text(title.title) },
                        onClick = {
                            selectedDoctorTitle = title
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Session Price
        OutlinedTextField(
            value = sessionPrice,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    sessionPrice = newValue
                }
            },
            label = { Text(stringResource(R.string.session_price)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            suffix = { Text("EGP") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Attachments Section
        Text(
            text = stringResource(R.string.attachments_pdf_png_jpeg_only),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Attachment Fields
        attachments.forEachIndexed { index, attachment ->
            AttachmentField(
                index = index,
                attachment = attachment,
                onSelectFile = {
                    filePickerLaunchers[index].launch("*/*")
                },
                onRemoveFile = {
                    attachments = attachments.toMutableList().apply {
                        this[index] = null
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Add Field Button (only show if less than 10 fields)
        if (attachments.size < maxAttachments) {
            OutlinedButton(
                onClick = {
                    attachments = attachments + null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.add_attachment_field))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                val validAttachments = attachments.filterNotNull()
                val parsedPrice = sessionPrice.toIntOrNull()

                if (selectedDoctorTitle != null &&
                    description.isNotBlank() &&
                    parsedPrice != null &&
                    parsedPrice > 0) {

                    onSubmit(
                        VerificationRequestsAction.SendVerificationRequest(
                            doctorTitle = selectedDoctorTitle ?: DoctorTitle.PSYCHIATRIST,
                            description = description,
                            sessionPrice = parsedPrice,
                            attachments = validAttachments
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.please_fill_in_all_required_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedDoctorTitle != null &&
                    description.isNotBlank() &&
                    sessionPrice.isNotBlank() &&
                    sessionPrice.toIntOrNull() != null &&
                    sessionPrice.toIntOrNull()!! > 0
        ) {
            Text(stringResource(R.string.submit_request))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}