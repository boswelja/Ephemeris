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
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.ui.CalendarPageLoader

@OptIn(ExperimentalAnimationApi::class)
@Composable
public fun EphemerisCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(DisplayDate) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    val coroutineScope = rememberCoroutineScope()
//    LaunchedEffect(pagerState) {
//        snapshotFlow { pagerState.page }.collect {
//            calendarState.currentMonth = calendarState.calendarPageSource.monthFor(it.toLong())
//        }
//    }
    val calendarPageLoader = remember(calendarState.calendarPageSource, calendarState.focusMode) {
        CalendarPageLoader(
            coroutineScope,
            calendarState.calendarPageSource,
            calendarState.focusMode
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
    pageData: List<List<DisplayDate>>,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(DisplayDate) -> Unit
) {
    Column(modifier) {
        pageData.forEach { week ->
            Row {
                week.forEach { date ->
                    Box(Modifier.weight(1f)) {
                        dayContent(date)
                    }
                }
            }
        }
    }
}
