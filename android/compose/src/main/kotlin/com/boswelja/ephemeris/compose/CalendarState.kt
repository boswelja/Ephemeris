package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth

interface CalendarState {
    var calendarPageLoader: CalendarPageLoader
    var currentMonth: YearMonth
}

internal class DefaultCalendarState(
    calendarPageLoader: CalendarPageLoader
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(calendarPageLoader.startDate.yearMonth)
    override var calendarPageLoader: CalendarPageLoader by mutableStateOf(calendarPageLoader)
}

@Composable
@Stable
fun rememberCalendarState(
    calendarPageLoader: () -> CalendarPageLoader
): CalendarState {
    return remember(calendarPageLoader) {
        DefaultCalendarState(calendarPageLoader())
    }
}
