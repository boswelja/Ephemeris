package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.chunked
import com.boswelja.ephemeris.core.endOfWeek
import com.boswelja.ephemeris.core.map
import com.boswelja.ephemeris.core.model.DisplayDate
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

/**
 * The core calendar page source interface. Calendar page sources are used to provide a layout to
 * calendar pages. See [CalendarMonthPageSource] and [CalendarWeekPageSource] for default
 * implementations.
 */
public interface CalendarPageSource {

    /**
     * Takes a page number and a DisplayDate producer, and returns a set of rows to display in the
     * calendar UI. This should not implement any caching itself, caching is handled by consumers.
     */
    public fun loadPageData(page: Int, transform: (LocalDate, YearMonth) -> DisplayDate): List<List<DisplayDate>>

    /**
     * Get the page number for the given date. This function should be as lightweight as possible,
     * as no results here are cached.
     */
    public fun getPageFor(date: LocalDate): Int
}

/**
 * An implementation of [CalendarPageSource] that loads full months. Total rows are dynamic, and
 * will change from month to month.
 */
public class CalendarMonthPageSource(
    private val firstDayOfWeek: DayOfWeek,
    private val startYearMonth: YearMonth = Clock.System.todayAt(TimeZone.currentSystemDefault()).yearMonth
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPageData(page: Int, transform: (LocalDate, YearMonth) -> DisplayDate): List<List<DisplayDate>> {
        val month = startYearMonth.plus(page)
        val firstDisplayedDate = month.startDate.startOfWeek(firstDayOfWeek)
        val lastDisplayedDate = month.endDate.endOfWeek(firstDayOfWeek)
        return (firstDisplayedDate..lastDisplayedDate)
            .chunked(daysInWeek)
            .map { dates ->
                dates.map { transform(it, month) }
            }
    }

    override fun getPageFor(date: LocalDate): Int {
        return startYearMonth.until(date.yearMonth)
    }
}

/**
 * An implementation of [CalendarPageSource] that loads one week per page.
 */
public class CalendarWeekPageSource(
    private val firstDayOfWeek: DayOfWeek,
    private val startDate: LocalDate = Clock.System.todayAt(TimeZone.currentSystemDefault())
) : CalendarPageSource {
    private val daysInWeek = DayOfWeek.values().size

    override fun loadPageData(
        page: Int,
        transform: (LocalDate, YearMonth) -> DisplayDate
    ): List<List<DisplayDate>> {
        val startOfWeek = startDate.plus(page * daysInWeek, DateTimeUnit.DAY)
            .startOfWeek(firstDayOfWeek)
        val weekDays =  (startOfWeek..startOfWeek.plus(daysInWeek - 1, DateTimeUnit.DAY))
            .map { transform(it, it.yearMonth) }
        return listOf(weekDays)
    }

    override fun getPageFor(date: LocalDate): Int {
        return startDate.daysUntil(date) / daysInWeek
    }
}
