package com.boswelja.ephemeris.views

import android.annotation.SuppressLint
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

internal class CalendarPagerAdapter : InfinitePagerAdapter<CalendarPageViewHolder>() {

    var pageLoader: CalendarPageLoader? = null
        @SuppressLint("NotifyDataSetChanged") // The entire dataset is invalidated when this changes
        set(value) {
            if (value != null && field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    var dayBinder: CalendarDateBinder<ViewHolder>? = null
        @SuppressLint("NotifyDataSetChanged") // The entire dataset is invalidated when this changes
        set(value) {
            if (value != null && field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int {
        return if (pageLoader != null && dayBinder != null) {
            super.getItemCount()
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindHolder(holder: CalendarPageViewHolder, page: Int) {
        val pageState = pageLoader!!.getPageData(page)
        holder.bindDisplayRows(dayBinder!!, pageState)
    }
}

internal class CalendarPageViewHolder(
    private val binding: CalendarPageBinding
) : ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    private val rowBindingCache = mutableListOf<CalendarRowBinding>()
    private val dateCellViewHolderCache = mutableListOf<ViewHolder>()

    private var boundDateCells = 0

    private var dateBinder: CalendarDateBinder<ViewHolder>? = null
        set(value) {
            if (field != value) {
                field = value
                // Clear our cached date cells on binder change
                dateCellViewHolderCache.clear()
            }
        }

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<ViewHolder>,
        page: CalendarPage
    ) {
        dateBinder = dayBinder
        boundDateCells = 0
        binding.root.apply {
            removeAllViewsInLayout() // This avoids an extra call to requestLayout and invalidate
            page.rows.forEachIndexed { index, calendarRow ->
                val row = createPopulatedRow(this, calendarRow, index)
                addView(row)
            }
        }
    }

    private fun createPopulatedRow(
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
                addView(
                    createDayCell(this, it),
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        weight = 1f // Ensures a row of date cells will fill the view width
                    }
                )
            }
        }
    }

    private fun createDayCell(
        parent: ViewGroup,
        calendarDay: CalendarDay
    ): View {
        val viewHolder = dateCellViewHolderCache.getOrNull(boundDateCells) ?: run {
            dateBinder!!.onCreateViewHolder(inflater, parent).also {
                dateCellViewHolderCache.add(boundDateCells, it)
            }
        }
        dateBinder!!.onBindView(viewHolder, calendarDay)
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
