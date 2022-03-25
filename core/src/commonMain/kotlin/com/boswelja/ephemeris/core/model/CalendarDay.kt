package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

/**
 * Contains state information for a single day cell.
 * @param date The [LocalDate] of the day cell.
 * @param isFocusedDate Whether the day cell is for a date in the currently displayed month. For example,
 * the current month might be February, but January 31st is also displayed on the calendar. In this case,
 * January 31st will have this field set to false, whereas any dates in February will have this field set to true.
 */
public data class CalendarDay internal constructor(
    val date: LocalDate,
    val isFocusedDate: Boolean
)
