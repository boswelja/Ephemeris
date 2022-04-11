package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageSource

public interface CalendarState {
    public var calendarPageSource: CalendarPageSource
}

internal class DefaultCalendarState(
    calendarPageSource: CalendarPageSource
) : CalendarState {
    override var calendarPageSource: CalendarPageSource by mutableStateOf(calendarPageSource)
}

@Composable
@Stable
public fun rememberCalendarState(
    calendarPageSource: () -> CalendarPageSource
): CalendarState {
    return remember(calendarPageSource) {
        DefaultCalendarState(calendarPageSource())
    }
}
