package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarPage
import kotlin.properties.Delegates

public class CalendarPageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val dayViewHolderMap = mutableMapOf<Int, ViewHolder>()
    private val layoutInflater = LayoutInflater.from(context)

    public var calendarPage: CalendarPage? = null
        set(value) {
            if (field != value) {
                field = value
                if (field != null && calendarDateBinder != null) invalidate()
            }
        }

    public var calendarDateBinder: CalendarDateBinder<*>? = null
        set(value) {
            if (field != value) {
                field = value
                if (field != null && calendarPage != null) invalidate()
            }
        }

    private var dateCellWidthSpec by Delegates.notNull<Int>()
    private var dateCellHeightSpec by Delegates.notNull<Int>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Skip layout if we have no data
        if (calendarPage == null || calendarDateBinder == null)
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                // The calendar should be exactly this width. Split it up and measure each date cell
                // exactly.
                val exactWidth = MeasureSpec.getSize(widthMeasureSpec)
                // Sample the calendar page for the row width
                val colCount = calendarPage!!.rows.first().days.size
                val exactDateWidth = exactWidth / colCount
                dateCellWidthSpec = MeasureSpec.makeMeasureSpec(exactDateWidth, MeasureSpec.EXACTLY)
            }
            MeasureSpec.AT_MOST -> {
                val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val colCount = calendarPage!!.rows.first().days.size
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
                val rowCount = calendarPage!!.rows.size
                val exactDateHeight = exactHeight / rowCount
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(exactDateHeight, MeasureSpec.EXACTLY)
            }
            MeasureSpec.AT_MOST -> {
                val maxHeight = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val rowCount = calendarPage!!.rows.size
                val maxDateHeight = maxHeight / rowCount
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(maxDateHeight, MeasureSpec.AT_MOST)
            }
            else -> {
                // Assume unspecified
                dateCellHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        }

        val scrapView = (calendarDateBinder as? CalendarDateBinder<ViewHolder>)!!.onCreateViewHolder(layoutInflater, this)
        scrapView.itemView.measure(dateCellWidthSpec, dateCellHeightSpec)

        val calendarWidth = scrapView.itemView.measuredWidth * calendarPage!!.rows.first().days.count()
        val calendarHeight = scrapView.itemView.measuredHeight * calendarPage!!.rows.count()

        setMeasuredDimension(calendarWidth, calendarHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed) return
        if (dayViewHolderMap.isNotEmpty()) {
            calendarPage!!.rows.forEachIndexed { rowIndex, calendarRow ->
                calendarRow.days.forEachIndexed { dayIndex, calendarDay ->
                    val view = dayViewHolderMap[rowIndex * dayIndex + dayIndex]!!

                    val width = view.itemView.measuredWidth
                    val height = view.itemView.measuredHeight

                    view.itemView.layout(
                        width * dayIndex,
                        height * rowIndex,
                        width * (dayIndex + 1),
                        height * (rowIndex + 1)
                    )
                    view.itemView.post {
                        @Suppress("UNCHECKED_CAST")
                        (calendarDateBinder as? CalendarDateBinder<ViewHolder>)?.onBindView(view, calendarDay)
                    }
                }
            }
        } else {
            calendarPage!!.rows.forEachIndexed { rowIndex, calendarRow ->
                calendarRow.days.forEachIndexed { dayIndex, calendarDay ->
                    val view = (calendarDateBinder as? CalendarDateBinder<ViewHolder>)!!.onCreateViewHolder(layoutInflater, this)
                    view.itemView.measure(dateCellWidthSpec, dateCellHeightSpec)
                    addViewInLayout(
                        view.itemView,
                        -1,
                        view.itemView.layoutParams ?: generateDefaultLayoutParams(),
                        true
                    )
                    dayViewHolderMap[rowIndex * dayIndex + dayIndex] = view

                    val width = view.itemView.measuredWidth
                    val height = view.itemView.measuredHeight

                    view.itemView.layout(
                        l + (width * dayIndex),
                        t + (height * rowIndex),
                        l + (width * (dayIndex + 1)),
                        t + (height * (rowIndex + 1))
                    )
                    view.itemView.post {
                        @Suppress("UNCHECKED_CAST")
                        (calendarDateBinder as? CalendarDateBinder<ViewHolder>)?.onBindView(view, calendarDay)
                    }
                }
            }
        }
    }
}
