package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.endOfWeek
import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.plusMonths
import com.boswelja.ephemeris.core.startOfWeek
import com.boswelja.ephemeris.core.toList
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class DefaultCalendarPagingSource(
    private val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    private val firstDayOfWeek: DayOfWeek
) : CalendarPagingSource {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPage(page: Long, pageSize: PageSize): Set<DisplayRow> {
        return when (pageSize) {
            PageSize.MONTH -> loadMonthPage(page)
            PageSize.WEEK -> loadWeekPage(page)
        }
    }

    override fun monthFor(page: Long, pageSize: PageSize): YearMonth {
        val month = when (pageSize) {
            PageSize.MONTH -> YearMonth(startDate.year, startDate.month.plusMonths(page))
            PageSize.WEEK -> {
                val day = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
                    .startOfWeek(firstDayOfWeek)
                YearMonth(day.year, day.month)
            }
        }
        return month
    }

    private fun loadWeekPage(page: Long): Set<DisplayRow> {
        val startOfWeek = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
            .startOfWeek(firstDayOfWeek)
        val weekDays = (startOfWeek..startOfWeek.plus(daysInWeek - 1, DateTimeUnit.DAY))
            .toList()
            .toSet()
        return setOf(
            DisplayRow(weekDays)
        )
    }

    private fun loadMonthPage(page: Long): Set<DisplayRow> {
        val month = YearMonth(startDate.year, startDate.month.plusMonths(page))
        val firstDisplayedDate = month.startDate.startOfWeek(firstDayOfWeek)
        val lastDisplayedDate = month.endDate.endOfWeek(firstDayOfWeek)
        return (firstDisplayedDate..lastDisplayedDate)
            .toList()
            .chunked(daysInWeek)
            .map { DisplayRow(it.toSet()) }
            .toSet()
    }
}
