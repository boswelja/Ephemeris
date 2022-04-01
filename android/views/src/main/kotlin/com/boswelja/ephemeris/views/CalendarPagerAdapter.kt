package com.boswelja.ephemeris.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.model.CalendarRow
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.databinding.CalendarPageBinding
import com.boswelja.ephemeris.views.databinding.CalendarRowBinding
import com.boswelja.ephemeris.views.databinding.DateCellBinding
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

    var dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>? = null
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
) : RecyclerView.ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    private val rowBindingCache = mutableListOf<CalendarRowBinding>()

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        page: CalendarPage
    ) {
        binding.root.apply {
            removeAllViewsInLayout() // This avoids an extra call to requestLayout and invalidate
            page.rows.forEachIndexed { index, calendarRow ->
                val row = createPopulatedRow(dayBinder, calendarRow, index)
                addView(row)
            }
        }
    }

    private fun createPopulatedRow(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        row: CalendarRow,
        rowNum: Int
    ): LinearLayout {
        val binding = rowBindingCache.getOrNull(rowNum)?.apply {
            // We're recycling a view, make sure it's empty
            root.removeAllViewsInLayout()
        } ?: run {
            CalendarRowBinding.inflate(inflater, null, false).also {
                rowBindingCache.add(rowNum, it)
            }
        }
        return binding.root.apply {
            row.days.forEach {
                addView(createDayCell(dayBinder, it))
            }
        }
    }

    private fun createDayCell(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        calendarDay: CalendarDay
    ): View {
        return DateCellBinding.inflate(inflater, binding.root, false).root.apply {
            // TODO At least try to recycle views
            val viewHolder = dayBinder.onCreateViewHolder(inflater, this)
            dayBinder.onBindView(viewHolder, calendarDay)
            addView(viewHolder.itemView)
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
