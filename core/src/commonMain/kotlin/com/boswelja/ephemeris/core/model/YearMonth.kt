package com.boswelja.ephemeris.core.model

import com.boswelja.ephemeris.core.plusMonths
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
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

public fun YearMonth.plus(months: Long): YearMonth {
    val monthDelta = month.ordinal + months
    val yearDelta = if (monthDelta < 0) {
        (monthDelta / allMonths.size) - 1
    } else {
        (monthDelta / allMonths.size)
    }
    return copy(
        year = (yearDelta + year).toInt(),
        month = month.plusMonths(months)
    )
}

private val allMonths = Month.values()
