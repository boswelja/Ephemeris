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
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface CalendarState {
    val startDate: LocalDate
    val firstDayOfWeek: DayOfWeek

    var calendarPageLoader: CalendarPageLoader
    var currentMonth: YearMonth
}

internal class DefaultCalendarState(
    override val startDate: LocalDate,
    override val firstDayOfWeek: DayOfWeek,
    calendarPageLoader: CalendarPageLoader
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(startDate.yearMonth)
    override var calendarPageLoader: CalendarPageLoader by mutableStateOf(calendarPageLoader)
}

@Composable
@Stable
fun rememberCalendarState(
    firstDayOfWeek: DayOfWeek,
    startDate: LocalDate,
    calendarPageLoader: CalendarPageLoader
): CalendarState {
    return remember(startDate, firstDayOfWeek, calendarPageLoader) {
        DefaultCalendarState(startDate, firstDayOfWeek, calendarPageLoader)
    }
}
