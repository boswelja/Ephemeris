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

public abstract class CalendarState {
    public abstract var calendarPageLoader: CalendarPageLoader
    public abstract var focusMode: FocusMode
    public abstract var currentMonth: YearMonth
        internal set
}

internal class DefaultCalendarState(
    focusMode: FocusMode,
    calendarPageLoader: CalendarPageLoader
) : CalendarState() {
    override var currentMonth: YearMonth by mutableStateOf(calendarPageLoader.startDate.yearMonth)
    override var focusMode: FocusMode by mutableStateOf(focusMode)
    override var calendarPageLoader: CalendarPageLoader by mutableStateOf(calendarPageLoader)
}

@Composable
@Stable
public fun rememberCalendarState(
    focusMode: FocusMode,
    calendarPageLoader: () -> CalendarPageLoader
): CalendarState {
    return remember(calendarPageLoader) {
        DefaultCalendarState(focusMode, calendarPageLoader())
    }
}
