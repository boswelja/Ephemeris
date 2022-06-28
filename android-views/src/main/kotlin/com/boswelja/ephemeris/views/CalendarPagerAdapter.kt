package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.databinding.CalendarPageBinding
import com.boswelja.ephemeris.views.databinding.CalendarRowBinding
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter
import kotlinx.datetime.LocalDate

internal class CalendarPagerAdapter(
    val pageLoader: CalendarPageLoader,
    private val dateBinder: CalendarDateBinder<ViewHolder>
) : InfinitePagerAdapter<CalendarPageViewHolder>() {

    override fun pageToPosition(page: Int): Int {
        return pageLoader.calendarPageSource.mapPageToInternalPosition(page)
    }

    override fun positionToPage(position: Int): Int {
        return pageLoader.calendarPageSource.mapInternalPositionToPage(position)
    }

    override fun getItemCount(): Int = pageLoader.calendarPageSource.maxPageRange.count()

    // ViewType is returned as rowNum * 10 + colNum, which is the calendar matrix [rowNum x colNum]
    override fun getItemViewType(position: Int): Int {
        val page = positionToPage(position)
        val pageState = pageLoader.getPageData(page)

        val colNum = getColNum(pageState)
        val rowNum = pageState.rows.size
        return rowNum * 10 + colNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent).apply {
            // We create layout for a viewType in advance, it will improve performance
            // and make adjust view size dynamically possible.
            createRows(dateBinder, itemView as ViewGroup, viewType / 10, viewType % 10)
        }
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        // Performs a full update
        val page = positionToPage(position)
        val pageState = pageLoader.getPageData(page)

        holder.fullBindDisplayRows(dateBinder, pageState, getColNum(pageState))
    }

    override fun onBindViewHolder(
        holder: CalendarPageViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            // RecyclerView may group payloads, we need to flatten it
            var startDate: LocalDate? = null
            var endDate: LocalDate? = null
            payloads.forEach {
                @Suppress("UNCHECKED_CAST")
                it as ClosedRange<LocalDate>
                if (startDate == null || startDate!! > it.start) {
                    startDate = it.start
                }
                if (endDate == null || endDate!! < it.endInclusive) {
                    endDate = it.endInclusive
                }
            }
            val page = positionToPage(position)
            holder.updateBoundDisplayRows(
                pageLoader.getPageData(page),
                dateBinder,
                (startDate!!..endDate!!)
            )
        }
    }

    // We assume that pageSource is providing same number of days for each row.
    private fun getColNum(pageState: CalendarPage) = if (pageState.rows.isNotEmpty()) pageState.rows[0].days.size else 0
}

internal class CalendarPageViewHolder(
    binding: CalendarPageBinding
) : ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    private val dayViewHolderMap = mutableMapOf<Int, ViewHolder>()

    fun updateBoundDisplayRows(
        page: CalendarPage,
        dateBinder: CalendarDateBinder<ViewHolder>,
        dates: ClosedRange<LocalDate>
    ) {
        if (dates.start == dates.endInclusive) {
            // Single date, optimize work
            page.getFlatDetailsFor(dates.start)?.let { (index, calendarDate) ->
                dayViewHolderMap[index]?.run {
                    dateBinder.onBindView(
                        this,
                        calendarDate
                    )
                }
            }
        } else {
            // Multiple dates, optimize work
            page.forEachInRange(dates) { index, calendarDate ->
                dayViewHolderMap[index]?.run {
                    dateBinder.onBindView(
                        this,
                        calendarDate
                    )
                }
            }
        }
    }

    fun fullBindDisplayRows(
        dateBinder: CalendarDateBinder<ViewHolder>,
        page: CalendarPage,
        colNum: Int
    ) {
        page.rows.forEachIndexed { rowIndex, calendarRow ->
            calendarRow.days.forEachIndexed { dayIndex, calendarDay ->
                dayViewHolderMap[rowIndex * colNum + dayIndex]?.let {
                    dateBinder.onBindView(it, calendarDay) }
            }
        }
    }

    // Create a rowNum by colNum view matrix
    internal fun createRows(dateBinder: CalendarDateBinder<ViewHolder>, parent: ViewGroup, rowNum: Int, colNum: Int) {
        for (rowIndex in 0 until rowNum) {
            val rowBinding = CalendarRowBinding.inflate(inflater, parent, false).apply {
                for (dayIndex in 0 until colNum) {
                    val dayViewHolder = dateBinder.onCreateViewHolder(inflater, root)
                    dayViewHolderMap[rowIndex * colNum + dayIndex] = dayViewHolder
                    root.addView(
                        dayViewHolder.itemView,
                        (dayViewHolder.itemView.layoutParams as LinearLayout.LayoutParams).apply {
                            weight = 1f // Ensures a row of date cells will fill the view width
                        }
                    )
                }
            }
            parent.addView(rowBinding.root)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CalendarPageViewHolder {
            val view = CalendarPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CalendarPageViewHolder(view)
        }
    }
}
