package com.boswelja.ephemeris.core.model

/**
 * Contains information about how the calendar should display a single row.
 * @param days A list of [CalendarDay] to display in the calendar.
 */
public data class CalendarRow internal constructor(
    val days: List<CalendarDay>
)
