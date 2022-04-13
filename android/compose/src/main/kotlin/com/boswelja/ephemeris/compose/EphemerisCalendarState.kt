package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.core.ui.CalendarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

public abstract class EphemerisCalendarState : CalendarState {
    internal abstract val pagerState: InfinitePagerState
    internal abstract var pageLoader: CalendarPageLoader
}

public class CalendarStateImpl internal constructor(
    calendarPageSource: CalendarPageSource,
    private val coroutineScope: CoroutineScope,
    override val pagerState: InfinitePagerState
) : EphemerisCalendarState() {

    override var pageLoader by mutableStateOf(CalendarPageLoader(coroutineScope, calendarPageSource))

    @OptIn(ExperimentalCoroutinesApi::class)
    override val displayedDateRange: StateFlow<ClosedRange<LocalDate>> = snapshotFlow { pageLoader }
        .flatMapLatest { snapshotFlow { pagerState.page } }
        .map { pageLoader.getDateRangeFor(it) }
        .stateIn(
            coroutineScope,
            SharingStarted.Eagerly,
            pageLoader.getDateRangeFor(pagerState.page)
        )

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
        // We launch this in a coroutine to avoid any thread blocking
        coroutineScope.launch {
            val page = pageSource.getPageFor(date)
            pagerState.scrollToPage(page)
        }
    }

    override suspend fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        pagerState.animateScrollToPage(page)
    }
}

@Composable
@Stable
public fun rememberCalendarState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    calendarPageSource: () -> CalendarPageSource
): EphemerisCalendarState {
    val pagerState = rememberInfinitePagerState()
    return remember(calendarPageSource, coroutineScope, pagerState) {
        CalendarStateImpl(calendarPageSource(), coroutineScope, pagerState)
    }
}
