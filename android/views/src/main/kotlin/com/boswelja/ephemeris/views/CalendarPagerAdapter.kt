package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.data.CalendarPageSource
import com.boswelja.ephemeris.core.model.DisplayDate
import com.boswelja.ephemeris.core.model.DisplayRow

internal class CalendarPagerAdapter(
    private val pagingSource: CalendarPageSource,
    var dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>
) : RecyclerView.Adapter<CalendarPageViewHolder>() {

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        val page = (position - (Int.MAX_VALUE / 2)).toLong()
        val pageState = pagingSource.loadPage(page)
        holder.bindDisplayRows(dayBinder, pageState)
    }
}

internal class CalendarPageViewHolder(
    private val rootView: LinearLayout
) : RecyclerView.ViewHolder(rootView) {

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        rows: Set<DisplayRow>
    ) {
        rootView.apply {
            removeAllViews()
            rows.forEach {
                val row = createPopulatedRow(dayBinder, it)
                addView(row)
            }
        }
    }

    private fun createPopulatedRow(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        row: DisplayRow
    ): LinearLayout {
        return LinearLayout(itemView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            row.dates.forEach {
                addView(getOrCreateDayCell(dayBinder, it))
            }
        }
    }

    private fun getOrCreateDayCell(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        displayDate: DisplayDate
    ): View {
        val container = FrameLayout(itemView.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                weight = 1f
            }
        }
        // TODO At least try to recycle views
        val viewHolder = dayBinder.onCreateViewHolder(LayoutInflater.from(itemView.context), container)
        dayBinder.onBindView(viewHolder, displayDate)
        container.addView(viewHolder.itemView)
        return container
    }

    companion object {
        fun create(parent: ViewGroup): CalendarPageViewHolder {
            val view = LinearLayout(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }
            return CalendarPageViewHolder(view)
        }
    }
}
