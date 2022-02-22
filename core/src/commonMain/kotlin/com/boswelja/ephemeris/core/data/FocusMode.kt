package com.boswelja.ephemeris.core.data

import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.core.model.yearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

public interface FocusMode {
    public operator fun invoke(
        date: LocalDate,
        targetYearMonth: YearMonth
    ): Boolean
}

public object WeekdayFocusMode : FocusMode {
    private val weekends = setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean {
        return !weekends.contains(date.dayOfWeek)
    }
}

public object DisplayMonthFocusMode : FocusMode {
    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean {
        return date.yearMonth == targetYearMonth
    }
}

public object AllFocusMode : FocusMode {
    override fun invoke(date: LocalDate, targetYearMonth: YearMonth): Boolean = true
}
