package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.endOfWeek
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow
import com.boswelja.ephemeris.core.model.PageSize
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.plus
import com.boswelja.ephemeris.core.model.yearMonth
import com.boswelja.ephemeris.core.plusMonths
import com.boswelja.ephemeris.core.startOfWeek
import com.boswelja.ephemeris.core.toList
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

public class CalendarMonthPageSource(
    private val startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    private val firstDayOfWeek: DayOfWeek,
    override val focusMode: FocusMode
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size

    override val pageSize: PageSize = PageSize.MONTH

    override fun loadPage(page: Long): Set<DisplayRow> {
        val month = YearMonth(startDate.year, startDate.month.plusMonths(page))
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
