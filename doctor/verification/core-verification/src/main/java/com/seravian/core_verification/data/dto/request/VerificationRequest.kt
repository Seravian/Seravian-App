package com.seravian.core_verification.data.dto.request

import com.seravian.core_verification.domain.utils.DoctorTitle
import com.seravian.core_verification.domain.FileAttachment
import com.seravian.core_verification.domain.withReplacedBaseName
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import io.ktor.http.headersOf
import io.ktor.util.cio.readChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class VerificationRequest(
    val doctorTitle: DoctorTitle,
    val description: String,
    @Transient
    val attachments: List<Pair<String, FileAttachment>> = emptyList()
) {
    fun toMultiPartFormData(maxTotalSizeBytes: Long = 15_000_000): List<PartData> {
        attachments.forEach { fileAttachment ->
            if (!fileAttachment.second.isSupported()) {
                throw IllegalArgumentException("File '${fileAttachment.second.file.name}' has unsupported format. Only PDF, PNG, and JPEG files are allowed.")
            }
        }

        val totalSize = attachments.sumOf { it.second.file.length() }
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

        attachments.forEach { fileAttachment ->
            parts.add(
                PartData.FileItem(
                    provider = { fileAttachment.second.file.readChannel() },
                    dispose = {},
                    partHeaders = headersOf(
                        HttpHeaders.ContentDisposition to listOf(
                            ContentDisposition.File
                                .withParameter(ContentDisposition.Parameters.Name, "attachments")
                                .withParameter(
                                    ContentDisposition.Parameters.FileName,
                                    fileAttachment.second.file.withReplacedBaseName(fileAttachment.first)
                                )
                                .toString()
                        ),
                        HttpHeaders.ContentType to listOf(
                            fileAttachment.second.contentType
                        )
                    )
                )
            )
        }

        return parts
    }
}