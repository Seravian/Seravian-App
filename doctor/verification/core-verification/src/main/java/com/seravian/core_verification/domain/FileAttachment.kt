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

fun File.withReplacedBaseName(newBase: String): String {
    val original = this.name
    // split on the LAST dot, if any
    val idx = original.lastIndexOf('.')
    return if (idx >= 0) {
        // keep the dot and whatever comes after
        "$newBase${original.substring(idx)}"
    } else {
        // no extension
        newBase
    }
}