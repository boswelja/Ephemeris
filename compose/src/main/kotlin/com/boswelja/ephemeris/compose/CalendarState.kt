package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

interface CalendarState {
    val startDate: LocalDate
    val firstDayOfWeek: DayOfWeek

    var currentMonth: YearMonth
    var pageSize: PageSize
}

internal class DefaultCalendarState(
    override val startDate: LocalDate,
    override val firstDayOfWeek: DayOfWeek,
    initialPageSize: PageSize
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(startDate.yearMonth)
    override var pageSize: PageSize by mutableStateOf(initialPageSize)
}

@Composable
@Stable
fun rememberCalendarState(
    startDate: LocalDate,
    firstDayOfWeek: DayOfWeek,
    initialPageSize: PageSize
): CalendarState {
    return remember(startDate, firstDayOfWeek, initialPageSize) {
        DefaultCalendarState(startDate, firstDayOfWeek, initialPageSize)
    }
}
