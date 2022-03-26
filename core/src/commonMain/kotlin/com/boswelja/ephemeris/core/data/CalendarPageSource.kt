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

