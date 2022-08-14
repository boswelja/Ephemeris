package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate
import kotlin.properties.Delegates

public class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val layoutInflater = LayoutInflater.from(context)

    private val boundViewPool = mutableMapOf<LocalDate, ViewBinding>()
    private val recycledBindingPool = mutableListOf<ViewBinding>()

    private var dateCellWidthSpec by Delegates.notNull<Int>()
    private var dateCellHeightSpec by Delegates.notNull<Int>()

    private lateinit var pageLoader: CalendarPageLoader

    private var _pageSource: CalendarPageSource? = null
    public var pageSource: CalendarPageSource
        get() = _pageSource!!
        set(value) {
            _pageSource = value
            tryInit()
        }

    private var _dayAdapter: CalendarDayAdapter<ViewBinding>? = null
    public var dayAdapter: CalendarDayAdapter<ViewBinding>
        get() = _dayAdapter!!
        set(value) {
            _dayAdapter = value
            invalidateAll()
            tryInit()
        }

    public var currentPage: Int = 0
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Skip layout if we have no page loader
        if (!::pageLoader.isInitialized) return super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Get the current page to use row/column for measurement
        val page = pageLoader.getPageData(currentPage)

        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                // The calendar should be exactly this width. Split it up and measure each date cell
                // exactly.
                val exactWidth = MeasureSpec.getSize(widthMeasureSpec)
                // Sample the calendar page for the row width
                val colCount = page.rows.first().days.size
                val exactDateWidth = exactWidth / colCount
                dateCellWidthSpec = MeasureSpec.makeMeasureSpec(exactDateWidth, MeasureSpec.EXACTLY)
            }
            MeasureSpec.AT_MOST -> {
                val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val colCount = page.rows.first().days.size
                val maxDateWidth = maxWidth / colCount
                dateCellWidthSpec = MeasureSpec.makeMeasureSpec(maxDateWidth, MeasureSpec.AT_MOST)
            }
            else -> {
                // Assume unspecified
                dateCellWidthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        }

        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                // The calendar should be exactly this width. Split it up and measure each date cell
                // exactly.
                val exactHeight = MeasureSpec.getSize(heightMeasureSpec)
                // Sample the calendar page for the row width
                val rowCount = page.rows.size
                val exactDateHeight = exactHeight / rowCount
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(exactDateHeight, MeasureSpec.EXACTLY)
            }
            MeasureSpec.AT_MOST -> {
                val maxHeight = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val rowCount = page.rows.size
                val maxDateHeight = maxHeight / rowCount
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(maxDateHeight, MeasureSpec.AT_MOST)
            }
            else -> {
                // Assume unspecified
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        }

        val cellSize = measureRecycledView()

        val calendarWidth = cellSize.width * page.rows.first().days.count()
        val calendarHeight = cellSize.height * page.rows.count()

        setMeasuredDimension(calendarWidth, calendarHeight)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        // Skip layout if we have no page loader
        if (!::pageLoader.isInitialized) return

        layoutCalendarPage(currentPage, left, top, right, bottom)
    }

    private fun layoutCalendarPage(page: Int, left: Int, top: Int, right: Int, bottom: Int) {
        val pageData = pageLoader.getPageData(page)
        val maxWidth = right - left

        pageData.rows.forEachIndexed { rowIndex, calendarRow ->
            val maxDateWidth = maxWidth / calendarRow.days.size
            calendarRow.days.forEachIndexed { dayIndex, calendarDay ->
                val view = getOrCreateBinding(maxDateWidth)

                val width = view.root.measuredWidth
                val height = view.root.measuredHeight
                boundViewPool[calendarDay.date] = view
                addViewInLayout(
                    view.root,
                    -1,
                    view.root.layoutParams ?: generateDefaultLayoutParams(),
                    true
                )

                view.root.layout(
                    left + (width * dayIndex),
                    top + (height * rowIndex),
                    width * (dayIndex + 1),
                    height * (rowIndex + 1)
                )
                dayAdapter.onBindView(view, calendarDay)
            }
        }
    }

    private fun getOrCreateBinding(maxWidth: Int): ViewBinding {
        val binding = recycledBindingPool.removeLastOrNull()
            ?: dayAdapter.onCreateView(layoutInflater, this)
        val needsMeasure = binding.root.measuredWidth !in 1..maxWidth
        if (needsMeasure) {
            binding.root.measure(
                dateCellWidthSpec,
                dateCellHeightSpec
            )
        }
        return binding
    }

    private fun measureRecycledView(): Size {
        // Measure a recycled cell view
        val binding = recycledBindingPool.firstOrNull() ?: dayAdapter.onCreateView(layoutInflater, this)
        binding.root.measure(dateCellWidthSpec, dateCellHeightSpec)
        return Size(binding.root.measuredWidth, binding.root.measuredHeight)
    }

    private fun tryInit() {
        if (_pageSource == null || _dayAdapter == null) return

        pageLoader = CalendarPageLoader(
            CoroutineScope(Dispatchers.Default),
            pageSource
        )
        invalidate()
    }

    private fun invalidateAll() {
        recycledBindingPool.clear()
        boundViewPool.clear()
        removeAllViews()
    }
}
