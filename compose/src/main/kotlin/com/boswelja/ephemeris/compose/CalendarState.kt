package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.YearMonth

interface CalendarState {
    var currentMonth: YearMonth
}

internal class DefaultCalendarState(
    startMonth: YearMonth
) : CalendarState {
    override var currentMonth: YearMonth by mutableStateOf(startMonth)
}

@Composable
@Stable
fun rememberCalendarState(startMonth: YearMonth): CalendarState {
    return remember(startMonth) {
        DefaultCalendarState(startMonth)
    }
}