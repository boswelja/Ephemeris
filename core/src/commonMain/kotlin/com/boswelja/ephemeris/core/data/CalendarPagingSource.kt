package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.FocusMode
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth

public interface CalendarPagingSource {
    public fun loadPage(
        page: Long,
        pageSize: PageSize,
        focusMode: FocusMode
    ): Set<DisplayRow>

    public fun monthFor(page: Long, pageSize: PageSize): YearMonth
}
