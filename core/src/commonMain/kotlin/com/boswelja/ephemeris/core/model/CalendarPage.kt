package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

/**
 * Contains information about how the calendar should display a page.
 * @param rows A list of [CalendarRow] for the calendar to display.
 */
public data class CalendarPage internal constructor(
    val rows: List<CalendarRow>
) {
    public val size: Int = rows.size * rows.first().days.size

    public val firstDate: LocalDate = rows.first().days.first().date

    public val lastDate: LocalDate = rows.last().days.last().date

    public fun get(flatIndex: Int): CalendarDay {
        val row = flatIndex / rows.first().days.size
        val col = flatIndex % rows.first().days.size
        return rows[row].days[col]
    }

    public fun forEach(block: (row: Int, column: Int, date: CalendarDay) -> Unit) {
        rows.forEachIndexed { rowIndex, calendarRow ->
            calendarRow.days.forEachIndexed { colIndex, calendarDay ->
                block(rowIndex, colIndex, calendarDay)
            }
        }
    }
}
