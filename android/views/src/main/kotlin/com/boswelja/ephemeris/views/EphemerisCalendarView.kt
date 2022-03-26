package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.core.ui.CalendarState
import com.boswelja.ephemeris.views.pager.InfiniteAnimatingPager
import com.boswelja.ephemeris.views.pager.OnSnapPositionChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteAnimatingPager(context, attrs, defStyleAttr), CalendarState {

    private lateinit var coroutineScope: CoroutineScope

    private val calendarAdapter = CalendarPagerAdapter()

    private val _displayedDateRange = MutableStateFlow(
        calendarAdapter.pageLoader?.getDateRangeFor(currentPage) ?: LocalDate(1970, 1, 1)..LocalDate(1970, 1, 1)
    )

    private val pageChangeListener = object : OnSnapPositionChangeListener {
        override fun onSnapPositionChange(position: Int) {
            calendarAdapter.pageLoader?.getDateRangeFor(position)?.let { _displayedDateRange.tryEmit(it) }
        }
    }

    override val displayedDateRange: StateFlow<ClosedRange<LocalDate>> = _displayedDateRange

    override var pageSource: CalendarPageSource
        get() = calendarAdapter.pageLoader!!.calendarPageSource
        set(value) {
            calendarAdapter.pageLoader = CalendarPageLoader(
                coroutineScope,
                value
            )
        }

    override fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        scrollToPosition(page)
    }

    override suspend fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        smoothScrollToPosition(page)
    }

    val dayBinder: CalendarDateBinder<*>
        get() = calendarAdapter.dayBinder!!

    init {
        adapter = calendarAdapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        coroutineScope = CoroutineScope(Dispatchers.Default)
        registerSnapPositionChangeListener(pageChangeListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.cancel()
        unregisterSnapPositionChangeListener(pageChangeListener)
    }

    @Suppress("UNCHECKED_CAST")
    fun initCalendar(
        pageSource: CalendarPageSource = this.pageSource,
        dayBinder: CalendarDateBinder<*> = this.dayBinder
    ) {
        calendarAdapter.dayBinder = dayBinder as CalendarDateBinder<ViewHolder>
        calendarAdapter.pageLoader = CalendarPageLoader(
            coroutineScope,
            pageSource
        )
    }
}
