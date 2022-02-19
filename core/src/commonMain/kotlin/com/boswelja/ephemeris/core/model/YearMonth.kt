package com.boswelja.ephemeris.core.model

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
