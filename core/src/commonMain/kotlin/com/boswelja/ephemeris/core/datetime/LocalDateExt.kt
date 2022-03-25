package com.boswelja.ephemeris.core.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

internal fun LocalDate.startOfWeek(firstDayOfWeek: DayOfWeek): LocalDate {
    if (dayOfWeek == firstDayOfWeek) return this
    val offset = dayOfWeek.minusDays(firstDayOfWeek.value.toLong()).value
    return minus(offset, DateTimeUnit.DAY)
}

internal fun LocalDate.endOfWeek(firstDayOfWeek: DayOfWeek): LocalDate {
    val lastDayOfWeek = firstDayOfWeek.minusDays(1)
    if (dayOfWeek == lastDayOfWeek) return this
    val offset = lastDayOfWeek.minusDays(dayOfWeek.value.toLong()).value
    return plus(offset, DateTimeUnit.DAY)
}
