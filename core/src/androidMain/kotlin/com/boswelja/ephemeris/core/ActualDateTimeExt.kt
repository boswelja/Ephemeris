package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

actual fun Month.plusMonths(months: Long): Month {
    return this.plus(months)
}

actual fun DayOfWeek.minusDays(days: Long): DayOfWeek {
    return this.minus(days)
}
