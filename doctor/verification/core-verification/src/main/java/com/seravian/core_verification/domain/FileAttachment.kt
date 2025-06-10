package com.seravian.core_verification.domain

import java.io.File

data class FileAttachment(
    val file: File,
    val contentType: String = getContentType(file.extension)
) {
    fun isSupported(): Boolean {
        return file.extension in listOf("pdf", "png", "jpg", "jpeg")
    }
}

fun getContentType(extension: String): String {
    return when (extension) {
        "pdf" -> "application/pdf"
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        else -> "application/octet-stream"
    }
}