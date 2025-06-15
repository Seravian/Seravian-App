package com.greenvenom.core_ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal fun formatInstant(
    instant: Instant,
    withTime: Boolean,
    fullMonthName: Boolean,
    locale: Locale
): String {
    val monthPattern = if (fullMonthName) "MMMM" else "MMM"

    val pattern = if (withTime) {
        when {
            isArabicLocale(locale) -> "d $monthPattern، yyyy  h:mm a" // Arabic comma
            else -> "d $monthPattern, yyyy  h:mm a" // English comma
        }
    } else {
        when {
            isArabicLocale(locale) -> "d $monthPattern، yyyy" // Arabic comma
            else -> "d $monthPattern, yyyy" // English comma
        }
    }

    return DateTimeFormatter
        .ofPattern(pattern)
        .withZone(ZoneId.systemDefault())
        .withLocale(locale)
        .format(instant)
}

internal fun formatLocalDate(
    localDate: LocalDate,
    fullMonthName: Boolean,
    locale: Locale
): String {
    val monthPattern = if (fullMonthName) "MMMM" else "MMM"

    val pattern = when {
        isArabicLocale(locale) -> "d $monthPattern، yyyy" // Arabic comma
        else -> "d $monthPattern, yyyy" // English comma
    }

    return localDate.format(
        DateTimeFormatter.ofPattern(pattern, locale)
    )
}

private fun isArabicLocale(locale: Locale): Boolean {
    return locale.language == "ar"
}
