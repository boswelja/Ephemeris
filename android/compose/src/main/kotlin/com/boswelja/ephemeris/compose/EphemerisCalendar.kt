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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun EphemerisCalendar(
    calendarState: EphemerisCalendarState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit
) {
    AnimatedContent(targetState = calendarState.pageLoader) { pageLoader ->
        InfiniteHorizontalPager(
            modifier = modifier.fillMaxWidth(),
            state = calendarState.pagerState,
            contentPadding = contentPadding
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
