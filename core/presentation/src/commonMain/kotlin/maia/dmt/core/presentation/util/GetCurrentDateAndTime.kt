package maia.dmt.core.presentation.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun getCurrentFormattedDateTime(
    now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): String {
    val day = now.day.toString().padStart(2, '0')
    val month = now.month.number.toString().padStart(2, '0')
    val year = now.year.toString()
    val hour = now.hour.toString().padStart(2, '0')
    val minute = now.minute.toString().padStart(2, '0')
    val second = now.second.toString().padStart(2, '0')

    return "$year-$month-$day $hour:$minute:$second"
}

fun getCurrentDate(
    now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): String {
    val day = now.day.toString().padStart(2, '0')
    val month = now.month.number.toString().padStart(2, '0')
    val year = now.year.toString()
    return "$day/$month/$year"
}

fun getCurrentTime(
    now: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): String {
    val hour = now.hour.toString().padStart(2, '0')
    val minute = now.minute.toString().padStart(2, '0')
    return "$hour:$minute"
}