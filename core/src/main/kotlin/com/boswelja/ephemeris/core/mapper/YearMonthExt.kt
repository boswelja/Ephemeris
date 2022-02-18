package com.boswelja.ephemeris.core.mapper

import com.boswelja.ephemeris.core.model.DisplayMonth
import com.boswelja.ephemeris.core.model.DisplayWeek
import com.boswelja.ephemeris.core.model.YearMonth
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun YearMonth.toDisplayMonth(
    firstDayOfWeek: DayOfWeek
): DisplayMonth {
    val firstDisplayedDate = calculateStartDate(this, firstDayOfWeek)
    val lastDisplayedDate = calculateEndDate(this, firstDisplayedDate)
    val weeks = (firstDisplayedDate..lastDisplayedDate)
        .toList()
        .chunked(DayOfWeek.values().size)
        .map { DisplayWeek(it.toSet()) }
        .toSet()
    return DisplayMonth(this, weeks)
}

private fun calculateStartDate(yearMonth: YearMonth, firstDayOfWeek: DayOfWeek): LocalDate {
    // If we're already on the first day of the week, we don't need to do anything
    if (yearMonth.startDate.dayOfWeek == firstDayOfWeek) return yearMonth.startDate

    val offset = yearMonth.startDate.dayOfWeek.minus(firstDayOfWeek.value.toLong()).value
    return yearMonth.startDate.minus(offset, DateTimeUnit.DAY)
}

private fun calculateEndDate(yearMonth: YearMonth, firstDisplayedDate: LocalDate): LocalDate {
    val monthEndDate = yearMonth.endDate
    val daysRemainder = firstDisplayedDate.daysUntil(monthEndDate.plus(1, DateTimeUnit.DAY)) % 7
    if (daysRemainder == 0) return monthEndDate

    val outDays = 7 - daysRemainder
    return monthEndDate.plus(outDays, DateTimeUnit.DAY)
}

private fun ClosedRange<LocalDate>.toList(): List<LocalDate> {
    val list = mutableListOf<LocalDate>()
    var currentItem = start
    while (currentItem <= endInclusive) {
        list.add(currentItem)
        currentItem = currentItem.plus(1, DateTimeUnit.DAY)
    }
    return list
}