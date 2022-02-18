package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth

interface CalendarState {
    var currentMonth: YearMonth
    var pageSize: PageSize
}

internal class DefaultCalendarState(
    startMonth: YearMonth,
    initialPageSize: PageSize
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(startMonth)
    override var pageSize: PageSize by mutableStateOf(initialPageSize)
}

@Composable
@Stable
fun rememberCalendarState(startMonth: YearMonth, initialPageSize: PageSize): CalendarState {
    return remember(startMonth) {
        DefaultCalendarState(startMonth, initialPageSize)
    }
}
