package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
import com.boswelja.ephemeris.views.databinding.RowDateCellBinding
import com.boswelja.ephemeris.views.databinding.RowDisplayDateBinding
import com.boswelja.ephemeris.views.databinding.RowPopulatedDateBinding

internal class CalendarPagerAdapter(
    pageLoader: CalendarPageLoader,
    dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>
) : RecyclerView.Adapter<CalendarPageViewHolder>() {

    var pageLoader: CalendarPageLoader = pageLoader
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    var dayBinder: CalendarDateBinder<RecyclerView.ViewHolder> = dayBinder
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        val page = (position - (Int.MAX_VALUE / 2))
        val pageState = pageLoader.getPageData(page)
        holder.bindDisplayRows(dayBinder, pageState)
    }
}

internal class CalendarPageViewHolder(
    private val binding: RowDisplayDateBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val inflater = LayoutInflater.from(itemView.context)

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        rows: List<List<DisplayDate>>
    ) {
        binding.root.removeAllViews()
        rows.forEach {
            binding.root.addView(createPopulatedRow(dayBinder, it))
        }
    }

    private fun createPopulatedRow(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        row: List<DisplayDate>
    ): LinearLayout {
        return RowPopulatedDateBinding.inflate(inflater, null, false).root.apply {
            row.forEach {
                addView(getOrCreateDayCell(dayBinder, it))
            }
        }
    }

    private fun getOrCreateDayCell(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        displayDate: DisplayDate
    ): LinearLayout {
        return RowDateCellBinding.inflate(inflater, binding.root, false).root.apply {
            // TODO At least try to recycle views
            val viewHolder = dayBinder.onCreateViewHolder(inflater, this)
            dayBinder.onBindView(viewHolder, displayDate)
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
