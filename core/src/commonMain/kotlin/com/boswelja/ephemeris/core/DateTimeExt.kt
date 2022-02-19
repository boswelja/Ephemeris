package com.boswelja.ephemeris.core

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

internal expect fun Month.plusMonths(months: Long): Month

internal expect fun DayOfWeek.minusDays(days: Long): DayOfWeek

internal val DayOfWeek.value: Int
    get() = ordinal + 1
