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
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.DefaultCalendarPageSource
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.FocusMode
import kotlinx.coroutines.flow.collect

@Composable
fun EphemerisCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    calendarPageSource: CalendarPageSource = DefaultCalendarPageSource(
        calendarState.startDate,
        calendarState.firstDayOfWeek,
        FocusMode.WEEKDAYS,
        calendarState.pageSize
    ),
    dayContent: @Composable BoxScope.(DisplayDate) -> Unit
) {
    val pagerState = rememberInfinitePagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.page }.collect {
            calendarState.currentMonth = calendarPageSource.monthFor(it.toLong())
        }
    }
    InfiniteHorizontalPager(
        modifier = modifier,
        state = pagerState
    ) {
        val pageData = remember(it, calendarState.pageSize) {
            calendarPageSource.loadPage(it.toLong())
        }
        Column {
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
}
