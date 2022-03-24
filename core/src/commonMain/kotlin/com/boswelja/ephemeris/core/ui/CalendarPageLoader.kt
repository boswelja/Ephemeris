package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.CalendarPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.math.abs

/**
 * Handles loading data from [CalendarPageSource], caching and prefetching entries. Platform UIs
 * should make use of this class for their data loading. If the page source or focus mode change, it
 * is expected a new instance will be created and the existing instance discarded. Prefetch and
 * cache operations are handled asynchronously via [coroutineScope].
 */
public class CalendarPageLoader(
    private val coroutineScope: CoroutineScope,
    public val calendarPageSource: CalendarPageSource
) {
    private val pageCache = mutableMapOf<Int, CalendarPage>()

    private val lastLoadedPage = MutableStateFlow(0)

    init {
        // Launch the page cache job
        coroutineScope.launch {
            lastLoadedPage
                .collect {
                    if (tryBuildCache(it)) {
                        trimCache(it)
                    }
                }
        }
    }

    /**
     * Loads [CHUNK_SIZE] pages into the cache from the given page [fromPage]. If [reverse] is true,
     * a chunk will be loaded behind the given page. Note that [fromPage] is excluded when caching.
     */
    private fun cacheChunk(fromPage: Int, reverse: Boolean) {
        // Iterate from the given page to the chunk size
        val range = if (reverse) {
            (fromPage - CHUNK_SIZE) until fromPage
        } else {
            (fromPage + 1)..(fromPage + CHUNK_SIZE)
        }
        range.forEach { page ->
            pageCache[page] = calendarPageSource.loadPageData(page)
        }
    }

    /**
     * Builds additional cache based around the given page if necessary.
     * @return true if cache was built on, false otherwise.
     */
    private fun tryBuildCache(page: Int): Boolean {
        val cacheForward = pageCache[page + PREFETCH_DISTANCE] == null
        val cacheBackward = pageCache[page - PREFETCH_DISTANCE] == null
        if (cacheForward) {
            cacheChunk(page, false)
        }
        if (cacheBackward) {
            cacheChunk(page, true)
        }
        return cacheForward || cacheBackward
    }

    /**
     * If the cache size is growing too large, trim the furthest elements from the given page.
     */
    private fun trimCache(page: Int) {
        if (pageCache.size > UPPER_CACHE_LIMIT) {
            val maxDistance = UPPER_CACHE_LIMIT / 2
            pageCache.keys
                .filter { abs(page - it) > maxDistance }
                .forEach {
                    pageCache.remove(it)
                }
        }
    }

    /**
     * Gets the data to display for the given page. If necessary, a cache load operation will be
     * started to ensure there's enough data available ahead of time.
     */
    public fun getPageData(page: Int): CalendarPage {
        lastLoadedPage.tryEmit(page)
        return pageCache[page] ?: calendarPageSource.loadPageData(page)
    }

    /**
     * Gets the range of dates displayed on the given page.
     */
    public fun getDateRangeFor(page: Int): ClosedRange<LocalDate> {
        // Cast to non-null here since in theory a page has already been loaded
        val pageData = pageCache[page]!!
        val startDate = pageData.rows.first().days.first().date
        val endDate = pageData.rows.last().days.last().date
        return startDate..endDate
    }

    public companion object {
        private const val PREFETCH_DISTANCE = 5
        private const val CHUNK_SIZE = 20
        private const val UPPER_CACHE_LIMIT = 250
    }
}
