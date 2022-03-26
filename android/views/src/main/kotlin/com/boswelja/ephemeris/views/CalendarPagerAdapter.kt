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
import com.boswelja.ephemeris.views.databinding.RowDateCellBinding
import com.boswelja.ephemeris.views.databinding.RowDisplayDateBinding
import com.boswelja.ephemeris.views.databinding.RowPopulatedDateBinding

internal class CalendarPagerAdapter : RecyclerView.Adapter<CalendarPageViewHolder>() {

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
            Int.MAX_VALUE
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        val page = (position - (Int.MAX_VALUE / 2))
        val pageState = pageLoader!!.getPageData(page)
        holder.bindDisplayRows(dayBinder!!, pageState)
    }
}

internal class CalendarPageViewHolder(
    private val binding: RowDisplayDateBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        page: CalendarPage
    ) {
        binding.root.apply {
            removeAllViews()
            page.rows.forEach {
                val row = createPopulatedRow(dayBinder, it)
                addView(row)
            }
        }
    }

    private fun createPopulatedRow(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        row: CalendarRow
    ): LinearLayout {
        return RowPopulatedDateBinding.inflate(inflater, null, false).root.apply {
            row.days.forEach {
                addView(createDayCell(dayBinder, it))
            }
        }
    }

    private fun createDayCell(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        calendarDay: CalendarDay
    ): View {
        return RowDateCellBinding.inflate(inflater, binding.root, false).root.apply {
            // TODO At least try to recycle views
            val viewHolder = dayBinder.onCreateViewHolder(inflater, this)
            dayBinder.onBindView(viewHolder, calendarDay)
            addView(viewHolder.itemView)
        }
    }

    companion object {
        fun create(parent: ViewGroup): CalendarPageViewHolder {
            val view = RowDisplayDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CalendarPageViewHolder(view)
        }
    }
}
