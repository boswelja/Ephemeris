package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.pager.InfiniteAnimatingPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate

public typealias DisplayedDateRangeChangeListener = (ClosedRange<LocalDate>) -> Unit

/**
 * A [android.view.View] that displays the Ephemeris calendar. Don't forget to call [initCalendar]
 * to initialize the calendar.
 */
public class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteAnimatingPager(context, attrs, defStyleAttr) {

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val calendarAdapter = CalendarPagerAdapter()

    private var displayedDateRangeChangeListener: DisplayedDateRangeChangeListener? = null

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

    public fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        scrollToPosition(page)
    }

    public fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        smoothScrollToPosition(page)
    }

    public fun setOnDisplayedDateRangeChangeListener(listener: DisplayedDateRangeChangeListener) {
        displayedDateRangeChangeListener = listener
    }

    /**
     * Initializes the calendar with [pageSource] and [dayBinder]. This must be called before
     * performing any operations.
     */
    @Suppress("UNCHECKED_CAST")
    public fun initCalendar(
        pageSource: CalendarPageSource,
        dayBinder: CalendarDateBinder<*>
    ) {
        calendarAdapter.dayBinder = dayBinder as CalendarDateBinder<ViewHolder>
        calendarAdapter.pageLoader = CalendarPageLoader(
            coroutineScope,
            pageSource
        )
        updateDisplayedDateRange(currentPage)
        scrollToPosition(currentPage)
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

    private fun updateDisplayedDateRange(page: Int) {
        displayedDateRange = calendarAdapter.pageLoader!!.getDateRangeFor(page)
        displayedDateRangeChangeListener?.let {
            it(displayedDateRange)
        }
    }
}
