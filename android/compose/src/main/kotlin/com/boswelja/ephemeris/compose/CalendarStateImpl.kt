package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

public class CalendarStateImpl internal constructor(
    calendarPageSource: CalendarPageSource
) : CalendarState {

    override val displayedDateRange: StateFlow<ClosedRange<LocalDate>>
        get() = TODO("Not yet implemented")

    override var pageSource: CalendarPageSource by mutableStateOf(calendarPageSource)

    override fun scrollToDate(date: LocalDate) {
        TODO("Not yet implemented")
    }

    override suspend fun animateScrollToDate(date: LocalDate) {
        TODO("Not yet implemented")
    }
}

@Composable
@Stable
public fun rememberCalendarState(
    calendarPageSource: () -> CalendarPageSource
): CalendarStateImpl {
    return remember(calendarPageSource) {
        CalendarStateImpl(calendarPageSource())
    }
}
