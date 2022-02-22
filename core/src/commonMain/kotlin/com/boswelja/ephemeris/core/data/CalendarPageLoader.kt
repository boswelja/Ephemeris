package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.endOfWeek
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.plus
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

public interface CalendarPageLoader {
    public fun loadPage(page: Long): Set<DisplayRow>

    public fun monthFor(page: Long): YearMonth
}

public class CalendarMonthPageLoader(
    private val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    private val firstDayOfWeek: DayOfWeek,
    private val focusMode: FocusMode
) : CalendarPageLoader {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPage(page: Long): Set<DisplayRow> {
        val month = startDate.yearMonth.plus(page)
        val firstDisplayedDate = month.startDate.startOfWeek(firstDayOfWeek)
        val lastDisplayedDate = month.endDate.endOfWeek(firstDayOfWeek)
        return (firstDisplayedDate..lastDisplayedDate)
            .toList()
            .chunked(daysInWeek)
            .map {
                DisplayRow(
                    it.map { date ->
                        val focused = focusMode(date, month)
                        DisplayDate(date, focused)
                    }.toSet()
                )
            }
            .toSet()
    }

    override fun monthFor(page: Long): YearMonth {
        return startDate.yearMonth.plus(page)
    }
}

public class CalendarWeekPageLoader(
    private val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    private val firstDayOfWeek: DayOfWeek,
    private val focusMode: FocusMode
) : CalendarPageLoader {
    private val daysInWeek = DayOfWeek.values().size

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
