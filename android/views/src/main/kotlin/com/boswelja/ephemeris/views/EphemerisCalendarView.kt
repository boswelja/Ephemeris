package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.AllFocusMode
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.YearMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.DayOfWeek

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewPager = RecyclerView(context, attrs).apply {
        id = generateViewId()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private val snapHelper = PagerSnapHelper()

    var pagingDataSource: CalendarPageLoader? = null
        set(value) {
            if (field == null || field != value) {
                field = value
                recreateAdapter()
            }
        }

    var focusMode: FocusMode = AllFocusMode
        set(value) {
            if (value != field) {
                field = value
                recreateAdapter()
            }
        }

    private val _currentMonth = MutableStateFlow<YearMonth?>(null)
    val currentMonth: Flow<YearMonth?> = _currentMonth

    var firstDayOfWeek: DayOfWeek = DayOfWeek.SUNDAY
        set(value) {
            if (field != value) {
                field = value
                recreateAdapter()
            }
        }

    var dayBinder: CalendarDateBinder<*>? = null
        set(value) {
            if (field == null || field != value) {
                field = value
                recreateAdapter()
            }
        }

    init {
        snapHelper.attachToRecyclerView(viewPager)
        addView(viewPager)

        /*
            Allow for XML attributes to be used to set initial config like app:firstDayOfWeek="1"
         */
        val config = context.theme.obtainStyledAttributes(attrs, R.styleable.EphemerisCalendarView, 0, 0)
        try {
            val firstDayOfWeek = config.getInteger(R.styleable.EphemerisCalendarView_firstDayOfWeek, -1)
            if (firstDayOfWeek > -1) {
                this.firstDayOfWeek = DayOfWeek.of(firstDayOfWeek + 1)
            }
        } finally {
            config.recycle()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun recreateAdapter() {
        if (dayBinder == null) return
        if (pagingDataSource == null) {
            pagingDataSource = CalendarMonthPageLoader(
                firstDayOfWeek
            )
        }

        viewPager.adapter = CalendarPagerAdapter(
            pagingDataSource!!,
            focusMode,
            dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
        )
    }
}
