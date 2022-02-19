package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

expect fun Month.plusMonths(months: Long): Month

expect fun DayOfWeek.minusDays(days: Long): DayOfWeek

val DayOfWeek.value: Int
    get() = ordinal + 1
