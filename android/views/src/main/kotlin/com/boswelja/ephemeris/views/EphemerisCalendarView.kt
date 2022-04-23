package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.pager.InfiniteAnimatingPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate

/**
 * A listener to be called when a date range changes.
 */
public typealias DateRangeChangeListener = (ClosedRange<LocalDate>) -> Unit

/**
 * A [android.view.View] that displays the Ephemeris calendar. Don't forget to call [initCalendar]
 * to initialize the calendar.
 */
public class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteAnimatingPager(context, attrs, defStyleAttr) {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val calendarAdapter = CalendarPagerAdapter()

    private var displayedDateRangeChangeListener: DateRangeChangeListener? = null

    /**
     * The date range currently displayed by the calendar.
     */
    public lateinit var displayedDateRange: ClosedRange<LocalDate>
        private set

    /**
     * The current [CalendarDateBinder] used to bind date cells. Setting this will cause the calendar
     * view to redraw itself.
     */
    public var dayBinder: CalendarDateBinder<*>
        get() = calendarAdapter.dayBinder!!
        set(value) {
            @Suppress("UNCHECKED_CAST")
            calendarAdapter.dayBinder = value as CalendarDateBinder<ViewHolder>
        }

    /**
     * The [CalendarPageSource] used to build and display pages in the calendar view. Setting this
     * will cause the calendar to recreate it's views.
     */
    public var pageSource: CalendarPageSource
        get() = calendarAdapter.pageLoader!!.calendarPageSource
        set(value) {
            calendarAdapter.pageLoader = CalendarPageLoader(
                coroutineScope,
                value
            )
            updateDisplayedDateRange(currentPage)
        }

    init {
        adapter = calendarAdapter
    }

    override fun onPageSnapping(page: Int) {
        super.onPageSnapping(page)
        updateDisplayedDateRange(page)
    }

    /**
     * Scrolls the calendar to the page with the given date.
     */
    public fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        scrollToPosition(page)
    }

    /**
     * Animates scrolling the calendar to the page with the given date.
     */
    public fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        smoothScrollToPosition(page)
    }

    /**
     * Sets a listener to be notified when [displayedDateRange] changes.
     */
    public fun setOnDisplayedDateRangeChangeListener(listener: DateRangeChangeListener) {
        displayedDateRangeChangeListener = listener
    }

    /**
     * Notifies the calendar that a single [date] has changed, and should be re-bound.
     */
    public fun notifyDateChanged(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        calendarAdapter.notifyItemChanged(pageToPosition(page))
    }

    /**
     * Notifies the calendar that a range of [dates] has changed, and should be re-bound.
     */
    public fun notifyDateRangeChanged(dates: ClosedRange<LocalDate>) {
        val startPage = pageSource.getPageFor(dates.start)
        val endPage = pageSource.getPageFor(dates.endInclusive)
        val itemsChanged = endPage - startPage + 1
        calendarAdapter.notifyItemRangeChanged(
            pageToPosition(startPage),
            itemsChanged
        )
    }

    /**
     * Updates the displayed date range for the given page. This will notify any listeners present.
     */
    private fun updateDisplayedDateRange(page: Int) {
        displayedDateRange = calendarAdapter.pageLoader!!.getDateRangeFor(page)
        displayedDateRangeChangeListener?.let {
            it(displayedDateRange)
        }
    }
}
