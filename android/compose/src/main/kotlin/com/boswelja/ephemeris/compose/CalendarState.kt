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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt

interface CalendarState {
    var calendarPageLoader: CalendarPageLoader
    var currentMonth: YearMonth
}

internal class DefaultCalendarState(
    startDate: LocalDate,
    calendarPageLoader: CalendarPageLoader
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(startDate.yearMonth)
    override var calendarPageLoader: CalendarPageLoader by mutableStateOf(calendarPageLoader)
}

@Composable
@Stable
fun rememberCalendarState(
    calendarPageLoader: CalendarPageLoader,
    startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault()),
): CalendarState {
    return remember(startDate, calendarPageLoader) {
        DefaultCalendarState(startDate, calendarPageLoader)
    }
}
