package com.boswelja.ephemeris.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.ui.CalendarPageLoader

/**
 * EphemerisCalendar displays a calendar Composable that takes configuration from [calendarState].
 * @param calendarState The [EphemerisCalendarState] used to control the calendar. See [rememberCalendarState]
 * for more information.
 * @param modifier The [Modifier] to be applied to the calendar Composable.
 * @param contentPadding The [PaddingValues] to apply to the calendar Composable. Content will not be
 * clipped to the padding when using this.
 * @param dayContent A Composable that takes a [CalendarDay] and renders a date cell on the calendar.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun EphemerisCalendar(
    pageSource: CalendarPageSource,
    calendarState: EphemerisCalendarState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pageLoader = remember(coroutineScope, pageSource) {
        CalendarPageLoader(coroutineScope, pageSource)
    }

    // Pass page source changes on to the calendar state
    LaunchedEffect(pageSource) {
        calendarState.pageSource = pageSource
    }

    AnimatedContent(targetState = pageLoader) { targetPageLoader ->
        val pagerState = rememberInfinitePagerState(
            pageCount = pageSource.maxPageRange.count(),
            calculatePageFromPosition = pageSource::mapInternalPositionToPage,
            calculatePositionFromPage = pageSource::mapPageToInternalPosition
        )
        LaunchedEffect(pagerState) {
            calendarState.pagerState = pagerState
        }

        // Notify calendar state of displayed date range changes
        LaunchedEffect(pagerState.page) {
            calendarState.displayedDateRange = pageLoader.getDateRangeFor(pagerState.page)
        }

        InfiniteHorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxWidth(),
            contentPadding = contentPadding
        ) {
            val pageData = remember {
                targetPageLoader.getPageData(it)
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
