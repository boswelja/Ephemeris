package com.boswelja.ephemeris.core.model

import kotlinx.datetime.LocalDate

@DslMarker
public annotation class CalendarPageDsl

/**
 * A builder function to create a [CalendarPage]. This is the entrypoint for the calendar page DSL.
 */
public fun calendarPage(init: CalendarPageBuilder.() -> Unit): CalendarPage {
    return CalendarPageBuilder().apply(init).build()
}

/**
 * A DSL Builder class to construct a [CalendarPage].
 */
@CalendarPageDsl
public class CalendarPageBuilder internal constructor() {
    private val rows = mutableListOf<CalendarRow>()

    /**
     * Creates a [CalendarRow] on this page. Note rows are ordered from top to bottom, based on
     * their call order.
     */
    public fun row(init: CalendarRowBuilder.() -> Unit) {
        val row = CalendarRowBuilder().apply(init).build()
        rows.add(row)
    }

    /**
     * Creates [count] number of [CalendarRow] on this page. Note rows are ordered from top to
     * bottom, based on their call order.
     */
    public fun rows(count: Int, init: CalendarRowBuilder.(Int) -> Unit) {
        repeat(count) {
            val row = CalendarRowBuilder()
                .apply { init(it) }
                .build()
            rows.add(row)
        }
    }

    internal fun build(): CalendarPage {
        require(rows.isNotEmpty())
        return CalendarPage(rows)
    }
}

/**
 * A DSL Builder class to construct a [CalendarRow].
 */
@CalendarPageDsl
public class CalendarRowBuilder internal constructor() {
    private val days = mutableListOf<CalendarDay>()

    /**
     * Creates a [CalendarDay] in this row. Note days are ordered from start to end, based on their
     * call order.
     */
    public fun day(init: CalendarDayBuilder.() -> Unit) {
        val day = CalendarDayBuilder().apply(init).build()
        days.add(day)
    }

    /**
     * Creates [count] number of [CalendarDay] in this row. Note days are ordered from start to end,
     * based on their call order.
     */
    public fun days(count: Int, init: CalendarDayBuilder.(Int) -> Unit) {
        repeat(count) {
            val day = CalendarDayBuilder()
                .apply { init(it) }
                .build()
            days.add(day)
        }
    }

    internal fun build(): CalendarRow {
        require(days.isNotEmpty())
        return CalendarRow(days)
    }
}

/**
 * A DSL Builder class to construct a [CalendarDay].
 */
@CalendarPageDsl
public class CalendarDayBuilder internal constructor() {
    private var date: LocalDate? = null
    private var focused: Boolean? = null

    /**
     * Set the date for this [CalendarDay].
     */
    public fun date(init: () -> LocalDate) { date = init() }

    /**
     * Sets whether this [CalendarDay] is in focus.
     */
    public fun focused(init: () -> Boolean) { focused = init() }

    internal fun build(): CalendarDay =
        CalendarDay(
            date!!,
            focused!!
        )
}
