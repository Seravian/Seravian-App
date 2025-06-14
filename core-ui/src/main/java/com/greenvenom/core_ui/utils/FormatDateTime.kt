package com.greenvenom.core_ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.Locale

fun formatDateTime(
    dateTime: String?,
    withTime: Boolean = true,
    fullMonthName: Boolean = false,
    locale: Locale = Locale.getDefault()
): String? {
    dateTime?.let {
        return try {
            when {
                // Handle ISO date with time and fractional seconds (e.g., "2023-12-21T10:30:45.123Z")
                dateTime.contains("T") && dateTime.contains(".") -> {
                    val cleanedTimestamp = dateTime.substringBefore(".") + "Z"
                    val instant = Instant.parse(cleanedTimestamp)
                    formatInstant(instant, withTime, fullMonthName, locale)
                }

                // Handle ISO date with time (e.g., "2023-12-21T10:30:45Z" or "2023-12-21T10:30:45")
                dateTime.contains("T") -> {
                    val cleanedTimestamp = if (!dateTime.endsWith("Z")) dateTime + "Z" else dateTime
                    val instant = Instant.parse(cleanedTimestamp)
                    formatInstant(instant, withTime, fullMonthName, locale)
                }

                // Handle ISO date only with Z suffix (e.g., "2003-12-21Z")
                dateTime.endsWith("Z") && dateTime.length == 11 -> {
                    val cleanDate = dateTime.dropLast(1) // Remove 'Z'
                    val localDate = LocalDate.parse(cleanDate)
                    formatLocalDate(localDate, fullMonthName, locale)
                }

                // Handle simple ISO date (e.g., "2003-12-21")
                dateTime.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) -> {
                    val localDate = LocalDate.parse(dateTime)
                    formatLocalDate(localDate, fullMonthName, locale)
                }

                else -> null
            }
        } catch (e: DateTimeParseException) {
            null
        }
    } ?: return null
}