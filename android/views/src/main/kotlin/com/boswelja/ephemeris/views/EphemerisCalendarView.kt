package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.data.FocusMode
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var coroutineScope: CoroutineScope

    private val viewPager = RecyclerView(context, attrs).apply {
        id = generateViewId()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    private val snapHelper = PagerSnapHelper()

    private var adapter: CalendarPagerAdapter? = null
        set(value) {
            if (value != null) {
                field = value
                viewPager.adapter = value
            }
        }

    val pageSource: CalendarPageSource
        get() = adapter!!.pageLoader.calendarPageSource

    val focusMode: FocusMode
        get() = adapter!!.pageLoader.focusMode

    val dayBinder: CalendarDateBinder<*>
        get() = adapter!!.dayBinder

    init {
        snapHelper.attachToRecyclerView(viewPager)
        addView(viewPager)
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
}
