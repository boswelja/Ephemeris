package com.boswelja.ephemeris.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.boswelja.ephemeris.core.data.CalendarPageSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.LocalDate

/**
 * The base class representing the Composable calendar state
 */
public abstract class EphemerisCalendarState {

    /**
     * The internal [InfinitePagerState] that [EphemerisCalendar] uses to control its page state.
     */
    internal lateinit var pagerState: InfinitePagerState

    /**
     * [EphemerisCalendar] provides & updates its page source for use within the calendar state.
     */
    internal lateinit var pageSource: CalendarPageSource

    /**
     * The currently displayed range of dates in the calendar view.
     */
    public abstract var displayedDateRange: ClosedRange<LocalDate>
        internal set

    /**
     * Scroll the calendar view to the page containing the given date.
     */
    public abstract suspend fun scrollToDate(date: LocalDate)

    /**
     * Animates scrolling the calendar view to the page containing the given date.
     */
    public abstract suspend fun animateScrollToDate(date: LocalDate)
}

/**
 * The default implementation of [EphemerisCalendarState]. This is returned by [rememberCalendarState]
 * @param coroutineScope A coroutine scope to use for running Calendar-related jobs.
 */
internal class EphemerisCalendarStateImpl internal constructor(
    private val coroutineScope: CoroutineScope
) : EphemerisCalendarState() {

    override var displayedDateRange by mutableStateOf(LocalDate(1990, 1, 1)..LocalDate(1990, 1, 1))

    override suspend fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        pagerState.scrollToPage(page)
    }

    override suspend fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        pagerState.animateScrollToPage(page)
    }
}

/**
 * Remembers an [EphemerisCalendarState] for use with [EphemerisCalendar]. Note any changes to parameters
 * will trigger recomposition, and the calendar state will be recreated.
 */
@Composable
@Stable
public fun rememberCalendarState(): EphemerisCalendarState {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    return remember(coroutineScope) {
        EphemerisCalendarStateImpl(coroutineScope)
    }
}
