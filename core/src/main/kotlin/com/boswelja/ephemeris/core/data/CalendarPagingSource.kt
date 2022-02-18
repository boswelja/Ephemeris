package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.YearMonth

interface CalendarDisplaySource {
    fun loadPage(
        page: Long,
        pageSize: PageSize
    ): Set<DisplayRow>

    fun monthFor(page: Long, pageSize: PageSize): YearMonth
}

enum class PageSize {
    MONTH,
    WEEK
}
