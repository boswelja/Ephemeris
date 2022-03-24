package com.boswelja.ephemeris.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.core.model.CalendarDay
import com.boswelja.ephemeris.core.ui.CalendarPageLoader

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
    private val rootView: LinearLayout
) : RecyclerView.ViewHolder(rootView) {

    fun bindDisplayRows(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        rows: List<List<CalendarDay>>
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
        row: List<CalendarDay>
    ): LinearLayout {
        return LinearLayout(itemView.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            row.forEach {
                addView(getOrCreateDayCell(dayBinder, it))
            }
        }
    }

    private fun getOrCreateDayCell(
        dayBinder: CalendarDateBinder<RecyclerView.ViewHolder>,
        calendarDay: CalendarDay
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
        dayBinder.onBindView(viewHolder, calendarDay)
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
