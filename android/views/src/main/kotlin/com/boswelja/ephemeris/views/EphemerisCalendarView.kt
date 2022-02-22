package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.data.WeekdayFocusMode
import com.boswelja.ephemeris.core.model.YearMonth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewPager = RecyclerView(context, attrs).apply {
        id = generateViewId()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private val snapHelper = PagerSnapHelper()

    private var pagingDataSource: CalendarPageLoader? = null

    private val _currentMonth = MutableStateFlow<YearMonth?>(null)
    val currentMonth: StateFlow<YearMonth?> = _currentMonth

    var firstDayOfWeek: DayOfWeek? = null
        set(value) {
            field = value
            recreateAdapter()
        }

    var dayBinder: CalendarDateBinder<*>? = null
        set(value) {
            field = value
            recreateAdapter()
        }

    init {
        snapHelper.attachToRecyclerView(viewPager)
        addView(viewPager)
    }

    @Suppress("UNCHECKED_CAST")
    private fun recreateAdapter() {
        if (firstDayOfWeek == null) return
        if (dayBinder == null) return
        pagingDataSource = CalendarMonthPageLoader(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            firstDayOfWeek!!,
            WeekdayFocusMode
        )
        viewPager.adapter = CalendarPagerAdapter(
            pagingDataSource!!,
            dayBinder!! as CalendarDateBinder<RecyclerView.ViewHolder>
        )
    }
}
