package com.boswelja.ephemeris.core.model

import com.boswelja.ephemeris.core.plusMonths
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.math.abs

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
    val yearChange = abs(months) / allMonths.size
    val newYear = if (months >= 0) year + yearChange else year - yearChange
    return copy(
        year = newYear.toInt(),
        month = month.plusMonths(months)
    )
}

private val allMonths = Month.values()