package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.model.CalendarRow
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.databinding.CalendarPageBinding
import com.boswelja.ephemeris.views.databinding.CalendarRowBinding
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter
import kotlinx.datetime.LocalDate

internal class CalendarPagerAdapter(
    val pageLoader: CalendarPageLoader,
    private val dateBinder: CalendarDateBinder<ViewHolder>
) : InfinitePagerAdapter<CalendarPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        // Performs a full update
        val page = positionToPage(position)
        val pageState = pageLoader.getPageData(page)
        holder.fullBindDisplayRows(dateBinder, pageState)
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
}

internal class CalendarPageViewHolder(
    private val binding: CalendarPageBinding
) : ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    private val rowBindingCache = mutableListOf<CalendarRowBinding>()
    private val dateCellViewHolderCache = mutableListOf<ViewHolder>()

    private var boundDateCells = 0

    fun updateBoundDisplayRows(
        page: CalendarPage,
        dateBinder: CalendarDateBinder<ViewHolder>,
        dates: ClosedRange<LocalDate>
    ) {
        val startIndex = page.getFlatIndexOf(dates.start)
            .coerceIn(0 until dateCellViewHolderCache.size)
        val endIndex = page.getFlatIndexOf(dates.endInclusive)
            .coerceIn(0 until dateCellViewHolderCache.size)
        (startIndex..endIndex).forEach {
            dateBinder.onBindView(
                dateCellViewHolderCache[it],
                page.getDateForFlatIndex(it)
            )
        }
    }

    fun fullBindDisplayRows(
        dateBinder: CalendarDateBinder<ViewHolder>,
        page: CalendarPage
    ) {
        boundDateCells = 0
        binding.root.apply {
            removeAllViewsInLayout() // This avoids an extra call to requestLayout and invalidate
            page.rows.forEachIndexed { index, calendarRow ->
                val row = createPopulatedRow(dateBinder, this, calendarRow, index)
                addView(row)
            }
        }
    }

    private fun createPopulatedRow(
        dateBinder: CalendarDateBinder<ViewHolder>,
        parent: ViewGroup,
        row: CalendarRow,
        rowNum: Int
    ): LinearLayout {
        val binding = rowBindingCache.getOrNull(rowNum)?.apply {
            // We're recycling a view, make sure it's empty
            root.removeAllViewsInLayout()
        } ?: run {
            CalendarRowBinding.inflate(inflater, parent, false).also {
                rowBindingCache.add(rowNum, it)
            }
        }
        return binding.root.apply {
            row.days.forEach {
                val view = createDayCell(dateBinder, this, it)
                addView(
                    view,
                    (view.layoutParams as LinearLayout.LayoutParams).apply {
                        weight = 1f // Ensures a row of date cells will fill the view width
                    }
                )
            }
        }
    }

    private fun createDayCell(
        dateBinder: CalendarDateBinder<ViewHolder>,
        parent: ViewGroup,
        calendarDay: CalendarDay
    ): View {
        val viewHolder = dateCellViewHolderCache.getOrNull(boundDateCells) ?: run {
            dateBinder.onCreateViewHolder(inflater, parent).also {
                dateCellViewHolderCache.add(boundDateCells, it)
            }
        }
        dateBinder.onBindView(viewHolder, calendarDay)
        boundDateCells += 1
        return viewHolder.itemView
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
