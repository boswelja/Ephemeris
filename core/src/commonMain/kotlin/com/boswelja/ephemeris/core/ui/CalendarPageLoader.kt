package com.boswelja.ephemeris.core.ui

import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow

public class CalendarPageLoader(
    public val calendarPageSource: CalendarPageSource,
    public val focusMode: FocusMode
) {
    public fun getPageData(page: Int): Set<DisplayRow> {
        return calendarPageSource.loadPageData(page) { date, month ->
            DisplayDate(
                date,
                focusMode(date, month)
            )
        }
    }
}
