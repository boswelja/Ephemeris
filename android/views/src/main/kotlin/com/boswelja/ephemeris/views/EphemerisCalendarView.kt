package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
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

    private val viewPager: ViewPager2
        get() = root.root

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

    private val page: Int
        get() = if (viewPager.currentItem <= 0) Int.MAX_VALUE / 2 else viewPager.currentItem

    init {
        // Attach our height adjuster to handle ViewPager2 height changes
        ViewPagerHeightAdjuster.attachTo(viewPager)
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
        var adapter = this.adapter
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
            adapter.dayBinder = dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
            adapter.pageLoader = CalendarPageLoader(
                coroutineScope,
                pageSource,
                focusMode
            )
        }

        setAdapter(adapter, page)
    }

    private fun <T : RecyclerView.ViewHolder> setAdapter(
        adapter: RecyclerView.Adapter<T>,
        page: Int = 0
    ) {
        viewPager.adapter = adapter
        viewPager.setCurrentItem(page, false)
    }
}
