package com.boswelja.ephemeris.core.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus

public data class YearMonth(
    val year: Int,
    val month: Month
) {
    val startDate: LocalDate = LocalDate(year, month, 1)
    val endDate: LocalDate = startDate.plus(DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
}

public val LocalDate.yearMonth: YearMonth
    get() = YearMonth(year, month)

public fun YearMonth.plus(months: Int): YearMonth {
    val localDate = LocalDate(year, month, 1)
    return localDate.plus(months, DateTimeUnit.MONTH)
        .yearMonth
}

public fun YearMonth.until(other: YearMonth): Int {
    if (this == other) return 0
    val yearDelta = -1 * (year - other.year)
    val monthDelta = -1 * (month.number - other.month.number) % allMonths.size
    return (yearDelta * allMonths.size) + monthDelta
}

private val allMonths = Month.values()
