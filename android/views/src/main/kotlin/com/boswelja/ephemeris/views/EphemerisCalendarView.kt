package com.boswelja.ephemeris.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.pager.HeightAdjustingPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate

/**
 * A listener to be called when a date range changes.
 */
public typealias DateRangeChangeListener = (ClosedRange<LocalDate>) -> Unit

/**
 * A [android.view.View] that displays the Ephemeris calendar. You must set a [dateBinder] and
 * [pageSource] for the calendar to work correctly.
 */
public class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // We capture any padding set on EphemerisCalendarView and pass it on to the inner pagers. This
    // allows clipToPadding to work correctly. Fortunately the calendar only ever scrolls horizontally
    private var internalPaddingLeft: Int = 0
    private var internalPaddingRight: Int = 0
    private var internalClipToPadding: Boolean = false

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private val currentPager: HeightAdjustingPager
        get() = getChildAt(0) as HeightAdjustingPager

    private lateinit var calendarAdapter: CalendarPagerAdapter

    private var _pageLoader: CalendarPageLoader? = null

    private var _dateBinder: CalendarDateBinder<RecyclerView.ViewHolder>? = null

    private var displayedDateRangeChangeListener: DateRangeChangeListener? = null

    /**
     * The date range currently displayed by the calendar.
     */
    public lateinit var displayedDateRange: ClosedRange<LocalDate>
        private set

    /**
     * Whether height changes should be animated for the calendar view. WARNING: This applies a
     * [LayoutTransition] to the parent ViewGroup. This might have unintended side-effects.
     */
    public var animateHeight: Boolean = false
        set(value) {
            if (value) {
                layoutTransition = LayoutTransition().apply {
                    this.enableTransitionType(LayoutTransition.CHANGING)
                }
                (parent as ViewGroup).layoutTransition = LayoutTransition().apply {
                    this.enableTransitionType(LayoutTransition.CHANGING)
                }
            } else {
                layoutTransition = null
                (parent as ViewGroup).layoutTransition = null
            }
            field = value
        }

    /**
     * The current [CalendarDateBinder] used to bind date cells. Setting this will cause the calendar
     * view to redraw itself.
     */
    public var dateBinder: CalendarDateBinder<*>
        get() = _dateBinder!!
        set(value) {
            @Suppress("UNCHECKED_CAST")
            _dateBinder = value as CalendarDateBinder<RecyclerView.ViewHolder>
            if (_dateBinder != null && _pageLoader != null) {
                calendarAdapter = CalendarPagerAdapter(_pageLoader!!, _dateBinder!!)
                initView()
            }
        }

    /**
     * The [CalendarPageSource] used to build and display pages in the calendar view. Setting this
     * will cause the calendar to recreate it's views.
     */
    public var pageSource: CalendarPageSource
        get() = _pageLoader!!.calendarPageSource
        set(value) {
            _pageLoader = CalendarPageLoader(
                coroutineScope,
                value
            )
            if (_dateBinder != null && _pageLoader != null) {
                calendarAdapter = CalendarPagerAdapter(_pageLoader!!, _dateBinder!!)
                initView()
            }
        }

    init {
        if (super.getPaddingLeft() > 0 || super.getPaddingRight() > 0) {
            internalPaddingLeft = super.getPaddingLeft()
            internalPaddingRight = super.getPaddingRight()
            super.setPadding(0, super.getPaddingTop(), 0, super.getPaddingBottom())
        }
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        internalPaddingLeft = left
        internalPaddingRight = right
        super.setPadding(0, top, 0, bottom)
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        when (layoutDirection) {
            LAYOUT_DIRECTION_RTL -> setPadding(end, top, start, bottom)
            else -> setPadding(start, top, end, bottom)
        }
    }

    override fun getPaddingStart(): Int {
        return currentPager.paddingStart
    }

    override fun getPaddingLeft(): Int {
        return currentPager.paddingLeft
    }

    override fun getPaddingEnd(): Int {
        return currentPager.paddingEnd
    }

    override fun getPaddingRight(): Int {
        return currentPager.paddingRight
    }

    override fun setClipToPadding(clipToPadding: Boolean) {
        internalClipToPadding = clipToPadding
    }

    override fun getClipToPadding(): Boolean {
        return internalClipToPadding
    }

    /**
     * Scrolls the calendar to the page with the given date.
     */
    public fun scrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        currentPager.scrollToPosition(page)
    }

    /**
     * Animates scrolling the calendar to the page with the given date.
     */
    public fun animateScrollToDate(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        currentPager.smoothScrollToPosition(page)
    }

    /**
     * Sets a listener to be notified when [displayedDateRange] changes.
     */
    public fun setOnDisplayedDateRangeChangeListener(listener: DateRangeChangeListener) {
        displayedDateRangeChangeListener = listener
    }

    /**
     * Notifies the calendar that a single [date] has changed, and should be re-bound.
     */
    public fun notifyDateChanged(date: LocalDate) {
        val page = pageSource.getPageFor(date)
        calendarAdapter.notifyItemChanged(calendarAdapter.pageToPosition(page))
    }

    /**
     * Notifies the calendar that a range of [dates] has changed, and should be re-bound.
     */
    public fun notifyDateRangeChanged(dates: ClosedRange<LocalDate>) {
        val startPage = pageSource.getPageFor(dates.start)
        val endPage = pageSource.getPageFor(dates.endInclusive)
        val itemsChanged = endPage - startPage + 1
        calendarAdapter.notifyItemRangeChanged(
            calendarAdapter.pageToPosition(startPage),
            itemsChanged
        )
    }

    private fun initView() {
        removeAllViews()
        val newView = HeightAdjustingPager(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            clipToPadding = internalClipToPadding
            setPadding(internalPaddingLeft, 0, internalPaddingRight, 0)
            adapter = calendarAdapter
            setOnSnapPositionChangeListener { updateDisplayedDateRange(it) }
        }
        addView(newView)
        updateDisplayedDateRange(currentPager.currentPage)
    }

    /**
     * Updates the displayed date range for the given page. This will notify any listeners present.
     */
    private fun updateDisplayedDateRange(page: Int) {
        displayedDateRange = calendarAdapter.pageLoader.getDateRangeFor(page)
        displayedDateRangeChangeListener?.let {
            it(displayedDateRange)
        }
    }
}
