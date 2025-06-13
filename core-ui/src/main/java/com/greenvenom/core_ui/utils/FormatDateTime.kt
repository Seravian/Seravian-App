package com.greenvenom.core_ui.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDateTime(dateTime: String?, withTime: Boolean = true): String? {
    dateTime?.let {
        val cleanedTimestamp = if (dateTime.contains(".")) {
            dateTime.substringBefore(".") + "Z"
        } else {
            if (!dateTime.endsWith("Z")) dateTime + "Z" else dateTime
        }

        val instant = Instant.parse(cleanedTimestamp)
        return DateTimeFormatter
            .ofPattern(
                if (withTime) "d MMM, yyyy  h:mm a" // 2 May, 2025  2:30 PM
                else "d MMM, yyyy" // "5 Jun, 2023"
            )
            .withZone(ZoneId.systemDefault())
            .format(instant)
    } ?: return null
}