package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    public companion object {
        private const val ChunkSize = 20
    }
}
