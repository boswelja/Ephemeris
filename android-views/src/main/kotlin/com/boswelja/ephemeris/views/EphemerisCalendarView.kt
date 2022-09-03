package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.OverScroller
import androidx.viewbinding.ViewBinding
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.recycling.RecyclingViewGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.LocalDate
import kotlin.properties.Delegates

public class EphemerisCalendarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclingViewGroup<ViewBinding, CalendarDay>(context, attrs, defStyleAttr) {

    private val scroller = OverScroller(context)
    private val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(event: MotionEvent): Boolean {
            return true
        }

        override fun onShowPress(event: MotionEvent) {
        }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return false
        }

        override fun onScroll(
            initialEvent: MotionEvent,
            moveEvent: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            scroller.startScroll(
                initialEvent.x.toInt(),
                initialEvent.y.toInt(),
                distanceX.toInt(),
                distanceY.toInt()
            )
            postInvalidate()
            return true
        }

        override fun onLongPress(event: MotionEvent) {
        }

        override fun onFling(
            firstEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            scroller.fling(
                firstEvent.x.toInt(),
                firstEvent.y.toInt(),
                velocityX.toInt(),
                velocityY.toInt(),
                0,
                Int.MAX_VALUE,
                0,
                Int.MAX_VALUE
            )
            return true
        }

    }
    private val gestureDetector = GestureDetector(context, gestureListener)

    private val boundViewPool = mutableMapOf<LocalDate, ViewBinding>()

    private var dateCellWidthSpec by Delegates.notNull<Int>()
    private var dateCellHeightSpec by Delegates.notNull<Int>()

    private lateinit var pageLoader: CalendarPageLoader

    public var pageSource: CalendarPageSource
        get() = pageLoader.calendarPageSource
        set(value) {
            // TODO Don't use an orphaned CoroutineScope here
            pageLoader = CalendarPageLoader(
                CoroutineScope(Dispatchers.Default),
                value
            )
            invalidate()
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

        val cellSize = measureRecycledBinding(dateCellWidthSpec, dateCellHeightSpec)

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
        // Skip layout if nothing changed
        if (!changed) return
        // Skip layout if we have no page loader
        if (!::pageLoader.isInitialized) return

        // Layout current page
        layoutCalendarPage(currentPage, 0, 0)

        // Layout next page, if possible
        val nextPage = currentPage + 1
        if (nextPage <= pageSource.maxPageRange.last) {
            layoutCalendarPage(
                nextPage,
                width,
                0
            )
        }

        // Layout prev page, if possible
        val prevPage = currentPage + 1
        if (prevPage >= pageSource.maxPageRange.first) {
            layoutCalendarPage(
                prevPage,
                -width,
                0
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun layoutCalendarPage(page: Int, leftOffset: Int, topOffset: Int) {
        val pageData = pageLoader.getPageData(page)

        pageData.rows.forEachIndexed { rowIndex, calendarRow ->
            calendarRow.days.forEachIndexed { dayIndex, calendarDay ->
                val view = getOrCreateBinding(dateCellWidthSpec, dateCellHeightSpec)

                val width = view.root.measuredWidth
                val height = view.root.measuredHeight
                boundViewPool[calendarDay.date] = view

                view.root.layout(
                    leftOffset + (width * dayIndex),
                    topOffset + (height * rowIndex),
                    leftOffset + (width * (dayIndex + 1)),
                    topOffset + (height * (rowIndex + 1))
                )
                view.root.post { adapter.onBindView(view, calendarDay) }
            }
        }
    }
}
