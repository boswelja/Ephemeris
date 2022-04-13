package com.boswelja.ephemeris.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage

/**
 * EphemerisCalendar displays a calendar Composable that takes configuration from [calendarState].
 * @param calendarState The [EphemerisCalendarState] to use for controlling the calendar. This will
 * usually be created via [rememberCalendarState].
 * @param modifier The [Modifier] to be applied to the calendar Composable.
 * @param dayContent A Composable that takes a [CalendarDay] and renders a date cell on the calendar.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun EphemerisCalendar(
    calendarState: EphemerisCalendarState,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit
) {
    AnimatedContent(targetState = calendarState.pageLoader) { pageLoader ->
        InfiniteHorizontalPager(
            modifier = modifier.fillMaxWidth(),
            state = calendarState.pagerState
        ) {
            val pageData = remember {
                pageLoader.getPageData(it)
            }
            CalendarPage(
                pageData = pageData,
                dayContent = dayContent
            )
        }
    }
}

/**
 * Displays a page in the calendar. Content is rendered based on [pageData].
 */
@Composable
internal fun CalendarPage(
    pageData: CalendarPage,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit
) {
    Column(modifier) {
        pageData.rows.forEach { week ->
            Row {
                week.days.forEach { date ->
                    Box(Modifier.weight(1f)) {
                        dayContent(date)
                    }
                }
            }
        }
    }
}
