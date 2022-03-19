package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.databinding.LayoutViewpagerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var coroutineScope: CoroutineScope

    private val root = LayoutViewpagerBinding.inflate(LayoutInflater.from(context), this, true)

    private val vp by lazy { root.root }

    private var adapter: CalendarPagerAdapter? = null
        set(value) {
            if (value != null) {
                field = value
                setAdapter(value)
            }
        }

    val pageSource: CalendarPageSource
        get() = adapter!!.pageLoader.calendarPageSource

    val focusMode: FocusMode
        get() = adapter!!.pageLoader.focusMode

    val dayBinder: CalendarDateBinder<*>
        get() = adapter!!.dayBinder

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
                //this.firstDayOfWeek = DayOfWeek.of(firstDayOfWeek + 1)
            }
        } finally {
            config.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        coroutineScope = CoroutineScope(Dispatchers.Default)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    fun initCalendar(
        pageSource: CalendarPageSource = this.pageSource,
        focusMode: FocusMode = this.focusMode,
        dayBinder: CalendarDateBinder<*> = this.dayBinder
    ) {
        if (adapter == null) {
            adapter = CalendarPagerAdapter(
                CalendarPageLoader(
                    coroutineScope,
                    pageSource,
                    focusMode
                ),
                dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
            )
        } else {
            adapter!!.apply {
                this.dayBinder = dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
                this.pageLoader = CalendarPageLoader(
                    coroutineScope,
                    pageSource,
                    focusMode
                )
            }
        }
    }

    private fun <T : RecyclerView.ViewHolder> setAdapter(adapter: RecyclerView.Adapter<T>, page: Int = 0) {
        vp.adapter = adapter
        vp.setCurrentItem(page, false)
    }
}
