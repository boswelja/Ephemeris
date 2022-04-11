package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.core.ui.CalendarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

public class CalendarStateImpl internal constructor(
    calendarPageSource: CalendarPageSource,
    private val coroutineScope: CoroutineScope
) : CalendarState {

    internal val mutableDisplayedDateRange = MutableStateFlow(
        // TODO This default shouldn't exist
        LocalDate(1970, 1, 1)..LocalDate(1970, 1, 1)
    )

    internal var pageLoader by mutableStateOf(CalendarPageLoader(coroutineScope, calendarPageSource))
        private set

    override val displayedDateRange: StateFlow<ClosedRange<LocalDate>>
        get() = mutableDisplayedDateRange

    override var pageSource: CalendarPageSource = calendarPageSource
        set(value) {
            if (field != value) {
                field = value
                pageLoader = CalendarPageLoader(
                    coroutineScope,
                    value
                )
            }
        }

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
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    calendarPageSource: () -> CalendarPageSource
): CalendarStateImpl {
    return remember(calendarPageSource, coroutineScope) {
        CalendarStateImpl(calendarPageSource(), coroutineScope)
    }
}
