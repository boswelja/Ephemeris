package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.AllFocusMode
import com.boswelja.ephemeris.core.data.CalendarMonthPageLoader
import com.boswelja.ephemeris.core.data.CalendarPageLoader
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.model.YearMonth
import com.boswelja.ephemeris.views.databinding.LayoutViewpagerBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.DayOfWeek

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val root = LayoutViewpagerBinding.inflate(LayoutInflater.from(context), this, true)

    private val vp by lazy { root.root }

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
                firstDayOfWeek
            )
        }

        setAdapter(
            CalendarPagerAdapter(
                pagingDataSource!!,
                focusMode,
                dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
            ),
            vp.currentItem.let { if (it <= 0) Int.MAX_VALUE / 2 else it }
        )
    }

    private fun <T : RecyclerView.ViewHolder> setAdapter(adapter: RecyclerView.Adapter<T>, page: Int = 0) {
        vp.adapter = adapter
        vp.setCurrentItem(page, false)
    }
}
