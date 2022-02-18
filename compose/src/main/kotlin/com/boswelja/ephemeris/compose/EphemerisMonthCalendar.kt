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
import com.boswelja.ephemeris.core.DayState
import com.boswelja.ephemeris.core.buildCalendarMonth
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.DayOfWeek

@Composable
fun EphemerisMonthCalendar(
    calendarState: CalendarState,
    modifier: Modifier = Modifier,
    dayContent: @Composable RowScope.(DayState) -> Unit
) {
    // Grab the initial value of currentMonth to act as a starting index
    val initialMonth = remember(calendarState) {
        calendarState.currentMonth
    }

    val pagerState = rememberInfinitePagerState()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.page }.collect {
            calendarState.currentMonth = initialMonth.plusMonths(it)
        }
    }
    InfiniteHorizontalPager(
        modifier = modifier,
        state = pagerState
    ) {
        val month = remember(it) {
            initialMonth.plusMonths(it)
        }
        val monthData = remember(month) {
            month.buildCalendarMonth(DayOfWeek.MONDAY)
        }
        Column {
            monthData.forEach { days ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    days.forEach { date ->
                        dayContent(
                            DayState(
                                date,
                                date.month == month.month
                            )
                        )
                    }
                }
            }
        }
    }
}
