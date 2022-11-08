package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.CalendarPage
import kotlinx.datetime.LocalDate

/**
 * Handles loading data from [CalendarPageSource], caching and prefetching entries. Platform UIs
 * should make use of this class for their data loading. If the page source or focus mode change, it
 * is expected a new instance will be created and the existing instance discarded.
 */
public class CalendarPageLoader(
    public val calendarPageSource: CalendarPageSource
) {
    private val pageCache = mutableMapOf<Int, CalendarPage>()

    /**
     * Gets the data to display for the given page. If necessary, a cache load operation will be
     * started to ensure there's enough data available ahead of time.
     */
    public fun getPageData(page: Int): CalendarPage {
        return pageCache.getOrPut(page) { calendarPageSource.loadPageData(page) }
    }

    /**
     * Gets the range of focused dates displayed on the given page. If no dates are in focus on the
     * given page, falls back to the full range of dates displayed.
     */
    public fun getDateRangeFor(page: Int): ClosedRange<LocalDate> {
        val pageData = getPageData(page)
        val startDate = pageData.rows.first().days.firstOrDefault { it.isFocusedDate }.date
        val endDate = pageData.rows.last().days.lastOrDefault { it.isFocusedDate }.date
        return startDate..endDate
    }

    /**
     * Returns the first item matching the given predicate. If no matches are found, the first item in
     * the list is returned.
     */
    private fun <T> List<T>.firstOrDefault(predicate: (T) -> Boolean): T {
        return firstOrNull(predicate) ?: first()
    }

    /**
     * Returns the last item matching the given predicate. If no matches are found, the last item in the
     * list is returned.
     */
    private fun <T> List<T>.lastOrDefault(predicate: (T) -> Boolean): T {
        return lastOrNull(predicate) ?: last()
    }
}
