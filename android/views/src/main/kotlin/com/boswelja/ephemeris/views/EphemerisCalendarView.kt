package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.core.ui.CalendarState
import com.boswelja.ephemeris.views.databinding.LayoutViewpagerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CalendarState {

    private lateinit var coroutineScope: CoroutineScope

    private val root = LayoutViewpagerBinding.inflate(LayoutInflater.from(context), this, true)

    private var adapter: CalendarPagerAdapter? = null
        set(value) {
            if (value != null) {
                field = value
                setAdapter(value)
            }
        }

    private val viewPager: ViewPager2
        get() = root.root

    private val page: Int
        get() = if (viewPager.currentItem <= 0) Int.MAX_VALUE / 2 else viewPager.currentItem

    private val _displayedDateRange = MutableStateFlow(
        adapter?.pageLoader?.getDateRangeFor(page) ?: LocalDate(1970, 1, 1)..LocalDate(1970, 1, 1)
    )

    private val pageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            adapter?.pageLoader?.getDateRangeFor(page)?.let { _displayedDateRange.tryEmit(it) }
        }
    }

    override val displayedDateRange: StateFlow<ClosedRange<LocalDate>> = _displayedDateRange

    override var pageSource: CalendarPageSource
        get() = adapter!!.pageLoader.calendarPageSource
        set(value) {
            adapter!!.pageLoader = CalendarPageLoader(
                coroutineScope,
                value
            )
        }

    override fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        viewPager.setCurrentItem(page, false)
    }

    override suspend fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        viewPager.setCurrentItem(page, true)
    }

    val dayBinder: CalendarDateBinder<*>
        get() = adapter!!.dayBinder

    init {
        // Attach our height adjuster to handle ViewPager2 height changes
        ViewPagerHeightAdjuster.attachTo(viewPager)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        coroutineScope = CoroutineScope(Dispatchers.Default)
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        coroutineScope.cancel()
        viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    @Suppress("UNCHECKED_CAST")
    fun initCalendar(
        pageSource: CalendarPageSource = this.pageSource,
        dayBinder: CalendarDateBinder<*> = this.dayBinder
    ) {
        var adapter = this.adapter
        if (adapter == null) {
            adapter = CalendarPagerAdapter(
                CalendarPageLoader(
                    coroutineScope,
                    pageSource
                ),
                dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
            )
        } else {
            adapter.apply {
                this.dayBinder = dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
                this.pageLoader = CalendarPageLoader(
                    coroutineScope,
                    pageSource
                )
            }
        }
        adapter.dayBinder = dayBinder as CalendarDateBinder<RecyclerView.ViewHolder>
        adapter.pageLoader = CalendarPageLoader(
            coroutineScope,
            pageSource
        )

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
