package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode

public interface CalendarState {
    public var calendarPageSource: CalendarPageSource
    public var focusMode: FocusMode
}

internal class DefaultCalendarState(
    focusMode: FocusMode,
    calendarPageSource: CalendarPageSource
) : CalendarState {
    override var focusMode: FocusMode by mutableStateOf(focusMode)
    override var calendarPageSource: CalendarPageSource by mutableStateOf(calendarPageSource)
}

@Composable
@Stable
public fun rememberCalendarState(
    focusMode: FocusMode,
    calendarPageSource: () -> CalendarPageSource
): CalendarState {
    return remember(calendarPageSource) {
        DefaultCalendarState(focusMode, calendarPageSource())
    }
}
