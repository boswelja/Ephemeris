package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

/**
 * Contains information about how the calendar should display a page.
 * @param rows A list of [CalendarRow] for the calendar to display.
 */
public data class CalendarPage internal constructor(
    val rows: List<CalendarRow>
) {

    /**
     * Retrieves the "flat" index and CalendarDay for the given [date], or null if the date does.
     * not exist on this page
     *
     * Flat index refers to the index of the date, if the 2D Array of dates were to be flattened to
     * a 1D Array.
     */
    public fun getFlatDetailsFor(date: LocalDate): Pair<Int, CalendarDay>? {
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
    public fun forEachInRange(
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
}
