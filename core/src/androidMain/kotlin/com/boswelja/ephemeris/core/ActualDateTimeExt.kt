package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

internal actual fun Month.plusMonths(months: Long): Month {
    return this.plus(months)
}

internal actual fun DayOfWeek.minusDays(days: Long): DayOfWeek {
    return this.minus(days)
}
