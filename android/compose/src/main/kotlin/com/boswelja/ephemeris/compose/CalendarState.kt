package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth

interface CalendarState {
    var calendarPageLoader: CalendarPageLoader
    var focusMode: FocusMode
    var currentMonth: YearMonth
}

internal class DefaultCalendarState(
    focusMode: FocusMode,
    calendarPageLoader: CalendarPageLoader
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(calendarPageLoader.startDate.yearMonth)
    override var focusMode: FocusMode by mutableStateOf(focusMode)
    override var calendarPageLoader: CalendarPageLoader by mutableStateOf(calendarPageLoader)
}

@Composable
@Stable
fun rememberCalendarState(
    focusMode: FocusMode,
    calendarPageLoader: () -> CalendarPageLoader
): CalendarState {
    return remember(calendarPageLoader) {
        DefaultCalendarState(focusMode, calendarPageLoader())
    }
}
