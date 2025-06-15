package com.seravian.feat_verification.presentation.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arpitkatiyarprojects.countrypicker.CountryPicker
import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.feat_verification.R
import com.seravian.feat_verification.presentation.utils.createFileFromUri
import com.seravian.feat_verification.presentation.viewmodel.requests.VerificationRequestsAction
import com.togitech.ccp.data.CountryData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVerificationRequestContent(
    onDismiss: () -> Unit,
    onSubmit: (VerificationRequestsAction.SendVerificationRequest) -> Unit
) {
    var selectedDoctorTitle by remember { mutableStateOf<DoctorTitle?>(null) }
    var description by remember { mutableStateOf("") }
    var sessionPrice by remember { mutableStateOf("") }
    var doctorTimeZone by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var languages by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf(listOf<FileAttachment?>()) }
    var attachmentsNote by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val maxAttachments = 10
    val scrollState = rememberScrollState()

    // Initialize with 1 empty attachment slot
    LaunchedEffect(Unit) {
        attachments = List(1) { null }
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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            // Enhanced Header with drag handle
            Column {
                // Drag handle
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(36.dp)
                        .height(4.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.new_verification_request),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stringResource(R.string.fill_in_your_professional_details),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close_ic),
                            contentDescription = stringResource(R.string.close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form sections with cards
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Professional Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

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
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
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
                        placeholder = { Text("Brief description of your expertise...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
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
                        placeholder = { Text("e.g. 500") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        suffix = {
                            Text(
                                "EGP",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Personal Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nationality

                    CountryPicker(

                    ) {

                    }
                    OutlinedTextField(
                        value = nationality,
                        onValueChange = { nationality = it },
                        label = { Text("Nationality") },
                        placeholder = { Text("e.g. Egyptian") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Languages
                    OutlinedTextField(
                        value = languages,
                        onValueChange = { languages = it },
                        label = { Text("Languages") },
                        placeholder = { Text("e.g. Arabic, English, French") },
                        supportingText = { Text("Separate multiple languages with commas") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Attachments Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.attachment_ic),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Documents & Certificates",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "PDF, PNG, JPEG only",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                        if (index < attachments.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Add Field Button
                    if (attachments.size < maxAttachments) {
                        OutlinedButton(
                            onClick = {
                                attachments = attachments + null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Attachments Note
                    OutlinedTextField(
                        value = attachmentsNote,
                        onValueChange = { attachmentsNote = it },
                        label = { Text("Additional Notes (Optional)") },
                        placeholder = { Text("Any additional information about your documents...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Enhanced Submit Button
            Button(
                onClick = {
                    val validAttachments = attachments.filterNotNull()
                    val parsedPrice = sessionPrice.toIntOrNull()
                    val languagesList = languages.split(",").map { it.trim() }.filter { it.isNotEmpty() }

                    if (selectedDoctorTitle != null &&
                        description.isNotBlank() &&
                        parsedPrice != null &&
                        parsedPrice > 0 &&
                        doctorTimeZone.isNotBlank() &&
                        nationality.isNotBlank()) {

                        onSubmit(
                            VerificationRequestsAction.SendVerificationRequest(
                                doctorTitle = selectedDoctorTitle ?: DoctorTitle.PSYCHIATRIST,
                                description = description,
                                sessionPrice = parsedPrice,
                                doctorTimeZone = doctorTimeZone,
                                nationality = nationality,
                                languages = languagesList,
                                workingSchedule = emptyMap(), // TODO: Add working schedule UI
                                attachments = validAttachments,
                                attachmentsNote = attachmentsNote.takeIf { it.isNotBlank() }
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedDoctorTitle != null &&
                        description.isNotBlank() &&
                        sessionPrice.isNotBlank() &&
                        sessionPrice.toIntOrNull() != null &&
                        sessionPrice.toIntOrNull()!! > 0 &&
                        doctorTimeZone.isNotBlank() &&
                        nationality.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_send),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.submit_request),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateVerificationRequestContentPreview() {
    CreateVerificationRequestContent(
        onSubmit = {},
        onDismiss = {}
    )
}