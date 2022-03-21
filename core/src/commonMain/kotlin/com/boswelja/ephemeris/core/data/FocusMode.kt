package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * The core focus mode interface. "Focus" is a concept applied to individual dates on a calendar
 * page, where each date can be "in focus". Focus is tracked as a Boolean. See [WeekdayFocusMode],
 * [DisplayMonthFocusMode] and [AllFocusMode] for default implementations.
 */
public interface FocusMode {
    public operator fun invoke(
        date: LocalDate,
        targetYearMonth: YearMonth
    ): Boolean
}

/**
 * An implementation of [FocusMode] that marks weekdays (Mon-Fri) as "in focus".
 */
public object WeekdayFocusMode : FocusMode {
    private val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean {
        return !weekends.contains(date.dayOfWeek)
    }
}

/**
 * An implementation of [FocusMode] that marks in/out dates as "out of focus".
 */
public object DisplayMonthFocusMode : FocusMode {
    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean {
        return date.yearMonth == targetYearMonth
    }
}

/**
 * An implementation of [FocusMode] that marks all dates as "in focus".
 */
public object AllFocusMode : FocusMode {
    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean = true
}
