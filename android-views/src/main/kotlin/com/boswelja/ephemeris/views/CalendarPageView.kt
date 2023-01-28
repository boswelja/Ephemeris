package com.boswelja.ephemeris.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.until
import kotlin.properties.Delegates

internal class CalendarPageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val dayViewHolderMap = mutableMapOf<Int, ViewHolder>()
    private val layoutInflater = LayoutInflater.from(context)

    var calendarPage: CalendarPage? = null
        set(value) {
            if (field != value) {
                field = value
                if (field != null && calendarDateBinder != null) postInvalidate()
            }
        }

    var calendarDateBinder: CalendarDateBinder<*>? = null
        set(value) {
            if (field != value) {
                field = value
                dayViewHolderMap.clear()
                removeAllViews()
                if (field != null && calendarPage != null) postInvalidate()
            }
        }

    private var dateCellWidthSpec by Delegates.notNull<Int>()
    private var dateCellHeightSpec by Delegates.notNull<Int>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Skip measure if we have no data
        if (calendarPage == null || calendarDateBinder == null)
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        dateCellWidthSpec = when (val mode = MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY,
            MeasureSpec.AT_MOST -> {
                val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val colCount = calendarPage!!.rows.first().days.size
                val maxDateWidth = requestedWidth / colCount
                MeasureSpec.makeMeasureSpec(maxDateWidth, mode)
            }

            else -> {
                // Assume unspecified
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        }

        dateCellHeightSpec = when (val mode = MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY,
            MeasureSpec.AT_MOST -> {
                val requestedHeight = MeasureSpec.getSize(widthMeasureSpec)

                // Sample the calendar page for the row width
                val rowCount = calendarPage!!.rows.size
                val maxDateHeight = requestedHeight / rowCount
                MeasureSpec.makeMeasureSpec(maxDateHeight, mode)
            }

            else -> {
                // Assume unspecified
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            }
        }

        val scrapView = calendarDateBinder!!.onCreateViewHolder(layoutInflater, this)
        scrapView.itemView.measure(dateCellWidthSpec, dateCellHeightSpec)

        val calendarWidth = scrapView.itemView.measuredWidth * calendarPage!!.rows.first().days.count()
        val calendarHeight = scrapView.itemView.measuredHeight * calendarPage!!.rows.count()

        setMeasuredDimension(calendarWidth, calendarHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (!changed) return
        if (dayViewHolderMap.isNotEmpty()) {
            rebindAllDates {
                dayViewHolderMap.getValue(it)
            }
        } else {
            rebindAllDates {
                calendarDateBinder!!.onCreateViewHolder(layoutInflater, this).apply {
                    itemView.measure(dateCellWidthSpec, dateCellHeightSpec)
                    addViewInLayout(
                        itemView,
                        -1,
                        itemView.layoutParams ?: generateDefaultLayoutParams(),
                        true
                    )
                    dayViewHolderMap[it] = this
                }
            }
        }
    }

    fun rebindDates(dates: ClosedRange<LocalDate>) {
        if (dayViewHolderMap.isEmpty()) return
        val startIndex = calendarPage!!.firstDate.until(dates.start, DateTimeUnit.DAY)
            .coerceAtLeast(0)
        val endIndex = calendarPage!!.firstDate.until(dates.endInclusive, DateTimeUnit.DAY)
            .coerceAtMost(childCount - 1)
        for (index in startIndex..endIndex) {
            bindDate(dayViewHolderMap.getValue(index), calendarPage!!.get(index))
        }
    }

    private fun bindDate(viewHolder: ViewHolder, calendarDay: CalendarDay) {
        viewHolder.itemView.post {
            @Suppress("UNCHECKED_CAST")
            (calendarDateBinder as? CalendarDateBinder<ViewHolder>)?.onBindView(viewHolder, calendarDay)
        }
    }

    private fun rebindAllDates(
        getOrCreateViewHolder: (Int) -> ViewHolder
    ) {
        var index = 0
        calendarPage!!.forEach { rowIndex, colIndex, calendarDay ->
            val viewHolder = getOrCreateViewHolder(index)
            index++

            val width = viewHolder.itemView.measuredWidth
            val height = viewHolder.itemView.measuredHeight

            viewHolder.itemView.layout(
                width * colIndex,
                height * rowIndex,
                width * (colIndex + 1),
                height * (rowIndex + 1)
            )
            bindDate(viewHolder, calendarDay)
        }
    }
}
