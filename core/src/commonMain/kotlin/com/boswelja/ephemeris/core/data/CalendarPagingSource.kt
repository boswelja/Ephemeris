package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.FocusMode
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth

interface CalendarPagingSource {
    fun loadPage(
        page: Long,
        pageSize: PageSize,
        focusMode: FocusMode
    ): Set<DisplayRow>

    fun monthFor(page: Long, pageSize: PageSize): YearMonth
}
