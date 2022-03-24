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
    val monthDelta = month.ordinal + months
    val yearDelta = if (monthDelta < 0) {
        (monthDelta / allMonths.size) - 1
    } else {
        (monthDelta / allMonths.size)
    }
    return copy(
        year = (yearDelta + year),
        month = month.plusMonths(months)
    )
}

public fun YearMonth.until(other: YearMonth): Int {
    val yearDelta = year - other.year
    val monthDelta = month.plusMonths(-1 * other.month.number).number
    return (yearDelta * allMonths.size) + monthDelta
}

private val allMonths = Month.values()
