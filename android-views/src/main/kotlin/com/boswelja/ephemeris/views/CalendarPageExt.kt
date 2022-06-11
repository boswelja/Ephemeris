package com.boswelja.ephemeris.views

import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import kotlinx.datetime.LocalDate

/**
 * Retrieves the "flat" index and CalendarDay for the given [date], or null if the date does.
 * not exist on this page
 *
 * Flat index refers to the index of the date, if the 2D Array of dates were to be flattened to
 * a 1D Array.
 */
public fun CalendarPage.getFlatDetailsFor(date: LocalDate): Pair<Int, CalendarDay>? {
    var currIndex = 0
    rows.forEach { row ->
        row.days.forEach {
            if (it.date == date) {
                return currIndex to it
            }
            currIndex += 1
        }
    }
    return null
}

/**
 * Executes the given [block] on each date on this page that exists within [dateRange]
 */
public fun CalendarPage.forEachInRange(
    dateRange: ClosedRange<LocalDate>,
    block: (flatIndex: Int, CalendarDay) -> Unit
) {
    var currIndex = 0
    rows.forEach { row ->
        row.days.forEach {
            if (dateRange.contains(it.date)) {
                block(currIndex, it)
            }
            currIndex += 1
        }
    }
}
