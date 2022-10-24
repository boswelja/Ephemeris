package com.boswelja.ephemeris.core.model

/**
 * Contains information about how the calendar should display a page.
 * @param rows A list of [CalendarRow] for the calendar to display.
 */
public data class CalendarPage internal constructor(
    val rows: List<CalendarRow>
) {
    public val size: Int = rows.size * rows.first().days.size

    public fun forEach(block: (row: Int, column: Int, date: CalendarDay) -> Unit) {
        rows.forEachIndexed { rowIndex, calendarRow ->
            calendarRow.days.forEachIndexed { colIndex, calendarDay ->
                block(rowIndex, colIndex, calendarDay)
            }
        }
    }
}
