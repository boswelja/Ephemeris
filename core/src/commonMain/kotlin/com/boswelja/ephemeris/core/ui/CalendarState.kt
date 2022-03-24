package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

/**
 * A core interface to be implemented by calendar UIs across platforms. This guarantees a set of
 * consistent functionality.
 */
public interface CalendarState {

    /**
     * Flows the currently displayed date range.
     */
    public val displayedDateRange: StateFlow<ClosedRange<LocalDate>>

    /**
     * The current calendar page source. Setting this will cause the calendar to redraw with data
     * from the new page source.
     */
    public var pageSource: CalendarPageSource

    /**
     * Instantly scrolls to the given date. See [animateScrollToDate] for a smooth scrolling
     * implementation.
     */
    public fun scrollToDate(date: LocalDate)

    /**
     * Smooth scrolls to the given date. This function will suspend until scrolling is completed.
     */
    public suspend fun animateScrollToDate(date: LocalDate)
}
