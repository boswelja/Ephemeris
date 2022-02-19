package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.internal.JSJoda.DayOfWeek as jsDayOfWeek
import kotlinx.datetime.internal.JSJoda.Month as jsMonth

actual fun Month.plusMonths(months: Long): Month {
    val jodaMonth = jsMonth.of(number).plus(months)
    return Month(jodaMonth.value().toInt())
}

actual fun DayOfWeek.minusDays(days: Long): DayOfWeek {
    val jodaDayOfWeek = jsDayOfWeek.of(isoDayNumber).minus(days)
    return DayOfWeek(jodaDayOfWeek.value().toInt())
}
