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

public abstract class EphemerisCalendarState {
    /**
     * The internal [InfinitePagerState] that [EphemerisCalendar] uses to control its page state.
     */
    internal abstract val pagerState: InfinitePagerState

    internal abstract var pageSource: CalendarPageSource

    public abstract var displayedDateRange: ClosedRange<LocalDate>
        internal set

    public abstract suspend fun scrollToDate(date: LocalDate)

    public abstract suspend fun animateScrollToDate(date: LocalDate)
}

/**
 * The default implementation of [EphemerisCalendarState]. This is returned by [rememberCalendarState]
 * @param pagerState The default [CalendarPageSource] to use. This can be changed later.
 * @param coroutineScope A coroutine scope to use for running Calendar-related jobs.
 */
internal class EphemerisCalendarStateImpl internal constructor(
    private val coroutineScope: CoroutineScope,
    override val pagerState: InfinitePagerState
) : EphemerisCalendarState() {

    override var displayedDateRange by mutableStateOf(LocalDate(1990, 1, 1)..LocalDate(1990, 1, 1))

    override lateinit var pageSource: CalendarPageSource

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
    val pagerState = rememberInfinitePagerState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    return remember(coroutineScope, pagerState) {
        EphemerisCalendarStateImpl(coroutineScope, pagerState)
    }
}
