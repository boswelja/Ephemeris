package com.boswelja.ephemeris.core

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

internal fun <T> ClosedRange<LocalDate>.mapToSet(transform: (LocalDate) -> T): Set<T> {
    val list = mutableSetOf<T>()
    var currentItem = start
    while (currentItem <= endInclusive) {
        list.add(transform(currentItem))
        currentItem = currentItem.plus(1, DateTimeUnit.DAY)
    }
    return list
}

internal fun ClosedRange<LocalDate>.chunked(chunkSize: Int): List<List<LocalDate>> {
    val list = mutableListOf<List<LocalDate>>()
    var currentItem = start
    val workingList = mutableListOf<LocalDate>()
    while (currentItem <= endInclusive) {
        workingList.add(currentItem)
        currentItem = currentItem.plus(1, DateTimeUnit.DAY)
        // If we've completed a chunk, add it to the list
        if (workingList.count() >= chunkSize) {
            // We use toList here to create a copy of the list, otherwise calling clear will mutate the
            // stored list.
            list.add(workingList.toList())
            workingList.clear()
        }
    }
    // Add any remaining items
    if (workingList.isNotEmpty()) {
        list.add(workingList)
    }
    return list
}
