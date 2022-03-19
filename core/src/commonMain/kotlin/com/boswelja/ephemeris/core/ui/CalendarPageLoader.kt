package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

/**
 * Handles loading data from [CalendarPageSource], combining with [FocusMode], caching and
 * prefetching entries. Platform UIs should make use of this class for their data loading. If the
 * page source or focus mode change, it is expected a new instance will be created and the existing
 * instance discarded. Prefetch and cache operations are handled asynchronously via [coroutineScope].
 */
public class CalendarPageLoader(
    private val coroutineScope: CoroutineScope,
    public val calendarPageSource: CalendarPageSource,
    public val focusMode: FocusMode
) {
    private val pageCache = mutableMapOf<Int, Set<DisplayRow>>()

    init {
        cacheChunk(-10)
    }

    private fun cacheChunk(fromPage: Int) {
        coroutineScope.launch {
            // Iterate from the given page to the chunk size
            // TODO support caching backwards
            (fromPage until fromPage + ChunkSize).forEach { page ->
                pageCache[page] = calendarPageSource.loadPageData(page) { date, month ->
                    DisplayDate(
                        date,
                        focusMode(date, month)
                    )
                }
            }
        }
    }

    /**
     * Gets the data to display for the given page. If necessary, a cache load operation will be
     * started to ensure there's enough data available ahead of time.
     */
    public fun getPageData(page: Int): Set<DisplayRow> {
        val cachedData = pageCache[page]
        if (cachedData != null) return cachedData

        pageCache[page] = calendarPageSource.loadPageData(page) { date, month ->
            DisplayDate(
                date,
                focusMode(date, month)
            )
        }
        cacheChunk(page + 1)

        return pageCache[page]!!
    }

    /**
     * Gets the range of dates displayed on the given page.
     */
    public fun getDateRangeFor(page: Int): ClosedRange<LocalDate> {
        // Cast to non-null here since in theory a page has already been loaded
        val pageData = pageCache[page]!!
        val startDate = pageData.first().dates.first().date
        val endDate = pageData.last().dates.last().date
        return startDate..endDate
    }

    public companion object {
        private const val ChunkSize = 20
    }
}
