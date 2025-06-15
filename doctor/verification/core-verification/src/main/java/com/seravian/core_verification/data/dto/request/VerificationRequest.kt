package com.seravian.core_verification.data.dto.request

import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.WorkingTimeSlot
import com.seravian.core_verification.domain.utils.Day
import com.seravian.core_verification.domain.utils.DoctorTitle
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import io.ktor.util.cio.readChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Serializable
data class VerificationRequest(
    val doctorTitle: DoctorTitle,
    val description: String,
    val sessionPrice: Int,
    val doctorTimeZone: String,
    val nationality: String,
    val languages: List<String>,
    val workingSchedule: Map<Int, List<WorkingTimeSlot>>,
    @Transient
    val attachments: List<FileAttachment> = emptyList(),
    val attachmentsNote: String?
) {
    fun toMultiPartFormData(maxTotalSizeBytes: Long = 15_000_000): List<PartData> {
        attachments.forEach { fileAttachment ->
            if (!fileAttachment.isSupported()) {
                throw IllegalArgumentException("File '${fileAttachment.file.name}' has unsupported format. Only PDF, PNG, and JPEG files are allowed.")
            }
        }

        val totalSize = attachments.sumOf { it.file.length() }
        if (totalSize > maxTotalSizeBytes) {
            throw IllegalArgumentException("Total attachments size ($totalSize bytes) exceeds maximum allowed size ($maxTotalSizeBytes bytes)")
        }

        val parts = mutableListOf<PartData>()

        parts.add(
            PartData.FormItem(
                value = doctorTitle.ordinal.toString(),
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "title").toString()
                )
            )
        )

        parts.add(
            PartData.FormItem(
                value = description,
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "description").toString()
                )
            )
        )

        // Session Price
        parts.add(
            PartData.FormItem(
                value = sessionPrice.toString(),
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "sessionPrice").toString()
                )
            )
        )

        // Doctor Time Zone
        parts.add(
            PartData.FormItem(
                value = doctorTimeZone,
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "doctorTimeZone").toString()
                )
            )
        )

        // Nationality
        parts.add(
            PartData.FormItem(
                value = nationality,
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "nationality").toString()
                )
            )
        )

        // Languages (as JSON array or comma-separated string)
        parts.add(
            PartData.FormItem(
                value = Json.encodeToString(languages), // or languages.joinToString(",") if server expects comma-separated
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "languages").toString()
                )
            )
        )

        // Working Schedule (as JSON)
        parts.add(
            PartData.FormItem(
                value = Json.encodeToString(workingSchedule),
                dispose = {},
                partHeaders = headersOf(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "workingSchedule").toString()
                )
            )
        )

        // Attachments Note
        attachmentsNote?.let { note ->
            parts.add(
                PartData.FormItem(
                    value = note,
                    dispose = {},
                    partHeaders = headersOf(
                        HttpHeaders.ContentDisposition,
                        ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.Name, "attachmentsNote").toString()
                    )
                )
            )
        }

        // File Attachments
        attachments.forEach { fileAttachment ->
            parts.add(
                PartData.FileItem(
                    provider = { fileAttachment.file.readChannel() },
                    dispose = {},
                    partHeaders = headersOf(
                        HttpHeaders.ContentDisposition to listOf(
                            ContentDisposition.File
                                .withParameter(ContentDisposition.Parameters.Name, "attachments")
                                .withParameter(
                                    ContentDisposition.Parameters.FileName,
                                    fileAttachment.file.name
                                )
                                .toString()
                        ),
                        HttpHeaders.ContentType to listOf(
                            fileAttachment.contentType
                        )
                    )
                )
            )
        }

        return parts
    }
}