package com.boswelja.ephemeris.core.model

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

data class YearMonth(
    val year: Int,
    val month: Month
) {
    val startDate: LocalDate = LocalDate(year, month, 1)
    val endDate: LocalDate = startDate.plus(DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
}

fun Instant.toYearMonth(): YearMonth {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    return YearMonth(
        localDateTime.year,
        localDateTime.month
    )
}

val LocalDate.yearMonth: YearMonth
    get() = YearMonth(year, month)
