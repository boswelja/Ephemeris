package com.boswelja.ephemeris.core

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun LocalDate.startOfWeek(firstDayOfWeek: DayOfWeek): LocalDate {
    val offset = dayOfWeek.minus(firstDayOfWeek.value.toLong()).value
    return minus(offset, DateTimeUnit.DAY)
}

fun LocalDate.endOfWeek(firstDayOfWeek: DayOfWeek): LocalDate {
    val offset = firstDayOfWeek.minus(dayOfWeek.value.toLong()).value
    return plus(offset, DateTimeUnit.DAY)
}

fun ClosedRange<LocalDate>.toList(): List<LocalDate> {
    val list = mutableListOf<LocalDate>()
    var currentItem = start
    while (currentItem <= endInclusive) {
        list.add(currentItem)
        currentItem = currentItem.plus(1, DateTimeUnit.DAY)
    }
    return list
}
