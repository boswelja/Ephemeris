package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.data.WeekdayFocusMode
import com.boswelja.ephemeris.views.databinding.LayoutViewpagerBinding
import kotlinx.datetime.DayOfWeek

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val root = LayoutViewpagerBinding.inflate(LayoutInflater.from(context), this, true)

    private val vp by lazy { root.root }
    private val recyclerView by lazy {
        root.root.getChildAt(0) as RecyclerView
    }

    var pagingDataSource: CalendarPageLoader? = null
        set(value) {
            if (field == null || field != value) {
                field = value
                recreateAdapter()
            }
        }

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
        /*
            Allow for XML attributes to be used to set initial config like app:firstDayOfWeek="1"
         */
        val config =
            context.theme.obtainStyledAttributes(attrs, R.styleable.EphemerisCalendarView, 0, 0)
        try {
            val firstDayOfWeek =
                config.getInteger(R.styleable.EphemerisCalendarView_firstDayOfWeek, -1)
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
                firstDayOfWeek,
                WeekdayFocusMode
            )
        }

        setAdapter(
            CalendarPagerAdapter(
                pagingDataSource!!,
                dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
            ),
            pagingDataSource!!.pageForToday()
        )
    }

    private fun <T : RecyclerView.ViewHolder> setAdapter(adapter: RecyclerView.Adapter<T>, page: Long = 1) {
        vp.adapter = adapter
        vp.setCurrentItem(page.toInt(), false)

        recyclerView.addOnScrollListener(
            InfiniteScrollBehaviour(
                adapter.itemCount,
                recyclerView.layoutManager as LinearLayoutManager
            )
        )
    }
}
