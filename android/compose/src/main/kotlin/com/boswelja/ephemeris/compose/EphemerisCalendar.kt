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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.ui.CalendarPageLoader

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun EphemerisCalendar(
    calendarState: CalendarStateImpl,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    val coroutineScope = rememberCoroutineScope()

    val calendarPageLoader = remember(calendarState.pageSource) {
        CalendarPageLoader(
            coroutineScope,
            calendarState.pageSource
        )
    }
    AnimatedContent(targetState = calendarPageLoader) { pageLoader ->
        InfiniteHorizontalPager(
            modifier = modifier.fillMaxWidth(),
            state = pagerState
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
