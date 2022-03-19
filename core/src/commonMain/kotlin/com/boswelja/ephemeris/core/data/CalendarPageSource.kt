package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.chunked
import com.boswelja.ephemeris.core.endOfWeek
import com.boswelja.ephemeris.core.mapToSet
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.plus
import com.boswelja.ephemeris.core.model.until
import com.boswelja.ephemeris.core.model.yearMonth
import com.boswelja.ephemeris.core.startOfWeek
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.todayAt

public interface CalendarPageSource {
    public fun loadPageData(page: Int, transform: (LocalDate, YearMonth) -> DisplayDate): Set<DisplayRow>

    public fun getPageFor(date: LocalDate): Int
}

public class CalendarMonthPageSource(
    private val firstDayOfWeek: DayOfWeek,
    private val startYearMonth: YearMonth = Clock.System.todayAt(TimeZone.currentSystemDefault()).yearMonth
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPageData(page: Int, transform: (LocalDate, YearMonth) -> DisplayDate): Set<DisplayRow> {
        val month = startYearMonth.plus(page)
        val firstDisplayedDate = month.startDate.startOfWeek(firstDayOfWeek)
        val lastDisplayedDate = month.endDate.endOfWeek(firstDayOfWeek)
        return (firstDisplayedDate..lastDisplayedDate)
            .chunked(daysInWeek)
            .map { dates ->
                DisplayRow(
                    dates.map { transform(it, month) }.toSet()
                )
            }
            .toSet()
    }

    override fun getPageFor(date: LocalDate): Int {
        return startYearMonth.until(date.yearMonth)
    }
}

public class CalendarWeekPageSource(
    private val firstDayOfWeek: DayOfWeek,
    private val startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPageData(page: Int, transform: (LocalDate, YearMonth) -> DisplayDate): Set<DisplayRow> {
        val startOfWeek = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
            .startOfWeek(firstDayOfWeek)
        val weekDays = (startOfWeek..startOfWeek.plus(daysInWeek - 1, DateTimeUnit.DAY))
            .mapToSet { transform(it, it.yearMonth) }
        return setOf(
            DisplayRow(weekDays)
        )
    }

    override fun getPageFor(date: LocalDate): Int {
        return startDate.daysUntil(date) / daysInWeek
    }
}
