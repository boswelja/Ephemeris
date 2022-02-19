package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.data.CalendarPagingSource
import com.boswelja.ephemeris.core.model.DayState
import kotlinx.coroutines.flow.collect

@Composable
fun EphemerisCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    calendarPagingSource: CalendarPagingSource,
    dayContent: @Composable BoxScope.(DayState) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.page }.collect {
            calendarState.currentMonth = calendarPagingSource.monthFor(it.toLong(), calendarState.pageSize)
        }
    }
    InfiniteHorizontalPager(
        modifier = modifier,
        state = pagerState
    ) {
        val pageData = remember(it, calendarState.pageSize) {
            calendarPagingSource.loadPage(it.toLong(), calendarState.pageSize)
        }
        Column {
            pageData.forEach { week ->
                Row {
                    week.dates.forEach { date ->
                        Box(Modifier.weight(1f)) {
                            dayContent(
                                DayState(
                                    date,
                                    true
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
