package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.CalendarPage
import kotlinx.datetime.LocalDate

/**
 * The core calendar page source interface. Calendar page sources are used to provide a layout to
 * calendar pages. See [CalendarMonthPageSource] and [CalendarWeekPageSource] for default
 * implementations.
 */
public interface CalendarPageSource {

    /**
     * Whether this page source has dates that may overlap from one page to the next. Ephemeris may
     * use this to determine whether additional updates are necessary when changing a date.
     */
    public val hasOverlappingDates: Boolean

    /**
     * Defines the maximum number of pages to be displayed. Pages are zero-based, therefore negative
     * values represent pages before the start page, and positive values represent pages after the
     * start page. Note the total number of pages should not exceed [Int.MAX_VALUE].
     */
    public val maxPageRange: IntRange

    /**
     * Takes a page number and a DisplayDate producer, and returns a set of rows to display in the
     * calendar UI. This should not implement any caching itself, caching is handled by consumers.
     */
    public fun loadPageData(page: Int): CalendarPage

    /**
     * Get the page number for the given date. This function should be as lightweight as possible,
     * as no results here are cached.
     */
    public fun getPageFor(date: LocalDate): Int
}
