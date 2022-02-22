package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth
import com.boswelja.ephemeris.core.startOfWeek
import com.boswelja.ephemeris.core.toList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

public class CalendarWeekPageSource(
    private val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    private val firstDayOfWeek: DayOfWeek,
    override val focusMode: FocusMode
) : CalendarPageSource {

    private val daysInWeek = DayOfWeek.values().size

    override val pageSize: PageSize = PageSize.WEEK

    override fun loadPage(page: Long): Set<DisplayRow> {
        val startOfWeek = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
            .startOfWeek(firstDayOfWeek)
        val weekDays = (startOfWeek..startOfWeek.plus(daysInWeek - 1, DateTimeUnit.DAY))
            .toList()
            .map {
                val focused = focusMode(it, it.yearMonth)
                DisplayDate(it, focused)
            }
            .toSet()
        return setOf(
            DisplayRow(weekDays)
        )
    }

    override fun monthFor(page: Long): YearMonth {
        val day = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
            .startOfWeek(firstDayOfWeek)
        return YearMonth(day.year, day.month)
    }
}
