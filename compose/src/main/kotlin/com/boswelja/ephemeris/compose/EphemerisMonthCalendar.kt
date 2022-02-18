package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.data.CalendarDisplaySource
import com.boswelja.ephemeris.core.data.PageSize
import com.boswelja.ephemeris.core.model.DayState
import com.boswelja.ephemeris.core.model.MonthDayState
import kotlinx.coroutines.flow.collect

@Composable
fun EphemerisMonthCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    calendarDisplaySource: CalendarDisplaySource,
    dayContent: @Composable RowScope.(DayState) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.page }.collect {
            calendarState.currentMonth = calendarDisplaySource.monthFor(it.toLong(), PageSize.WEEK)
        }
    }
    InfiniteHorizontalPager(
        modifier = modifier,
        state = pagerState
    ) {
        val pageData = remember(it) {
            calendarDisplaySource.loadPage(it.toLong(), PageSize.WEEK)
        }
        Column {
            pageData.forEach { week ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.dates.forEach { date ->
                        dayContent(
                            MonthDayState(
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
