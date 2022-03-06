package com.boswelja.ephemeris.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EphemerisCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(DisplayDate) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.page }.collect {
            calendarState.currentMonth = calendarState.calendarPageLoader.monthFor(it.toLong())
        }
    }
    AnimatedContent(targetState = calendarState.calendarPageLoader) { pageLoader ->
        InfiniteHorizontalPager(
            modifier = modifier,
            state = pagerState
        ) {
            val pageData = remember {
                pageLoader.loadPage(it.toLong())
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
    pageData: Set<DisplayRow>,
    modifier: Modifier = Modifier,
    dayContent: @Composable BoxScope.(DisplayDate) -> Unit
) {
    Column(modifier) {
        pageData.forEach { week ->
            Row {
                week.dates.forEach { date ->
                    Box(Modifier.weight(1f)) {
                        dayContent(date)
                    }
                }
            }
        }
    }
}
