package com.boswelja.ephemeris.core

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

data class YearMonth(
    val year: Int,
    val month: Month
) {
    val startDate: LocalDate

    val days: Int

    init {
        // Calculate total days in month
        startDate = LocalDate(year, month, 1)
        val end = startDate.plus(DateTimeUnit.MONTH)
        days = startDate.until(end, DateTimeUnit.DAY)
    }
}

fun YearMonth.plusMonths(months: Int): YearMonth {
    return copy(
        month = month.plus(months.toLong())
    )
}

fun Instant.toYearMonth(): YearMonth {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return YearMonth(
        localDateTime.year,
        localDateTime.month
    )
}
