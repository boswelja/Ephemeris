package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.boswelja.ephemeris.core.DayState
import com.boswelja.ephemeris.core.YearMonth
import com.boswelja.ephemeris.core.buildCalendarMonth
import com.boswelja.ephemeris.core.plusMonths
import com.boswelja.ephemeris.core.toYearMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek

@Composable
fun EphemerisCalendar(
    modifier: Modifier = Modifier,
    startMonth: YearMonth = Clock.System.now().toYearMonth(),
    dayContent: @Composable RowScope.(DayState) -> Unit
) {
    InfiniteHorizontalPager(modifier) {
        val month = remember(it) {
            startMonth.plusMonths(it)
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
