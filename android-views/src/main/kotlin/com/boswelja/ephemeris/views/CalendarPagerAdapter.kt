package com.boswelja.ephemeris.views

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boswelja.ephemeris.core.model.CalendarPage
import com.boswelja.ephemeris.core.ui.CalendarPageLoader
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

        val colNum = pageState.rows.first().days.size
        val rowNum = pageState.rows.size
        return rowNum * colNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        // Performs a full update
        val page = positionToPage(position)
        val pageState = pageLoader.getPageData(page)

        holder.fullBindDisplayRows(dateBinder, pageState)
    }

//    override fun onBindViewHolder(
//        holder: CalendarPageViewHolder,
//        position: Int,
//        payloads: MutableList<Any>
//    ) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position)
//        } else {
//            // RecyclerView may group payloads, we need to flatten it
//            var startDate: LocalDate? = null
//            var endDate: LocalDate? = null
//            payloads.forEach {
//                @Suppress("UNCHECKED_CAST")
//                it as ClosedRange<LocalDate>
//                if (startDate == null || startDate!! > it.start) {
//                    startDate = it.start
//                }
//                if (endDate == null || endDate!! < it.endInclusive) {
//                    endDate = it.endInclusive
//                }
//            }
//            val page = positionToPage(position)
//            holder.updateBoundDisplayRows(
//                pageLoader.getPageData(page),
//                dateBinder,
//                (startDate!!..endDate!!)
//            )
//        }
//    }
}

internal class CalendarPageViewHolder(
    private val view: CalendarPageView
) : ViewHolder(view) {

    fun updateBoundDisplayRows(
        page: CalendarPage,
        dateBinder: CalendarDateBinder<ViewHolder>,
        dates: ClosedRange<LocalDate>
    ) {
        // TODO
//        if (dates.start == dates.endInclusive) {
//            // Single date, optimize work
//            page.getFlatDetailsFor(dates.start)?.let { (index, calendarDate) ->
//                dayViewHolderMap[index]?.run {
//                    dateBinder.onBindView(
//                        this,
//                        calendarDate
//                    )
//                }
//            }
//        } else {
//            // Multiple dates, optimize work
//            page.forEachInRange(dates) { index, calendarDate ->
//                dayViewHolderMap[index]?.run {
//                    dateBinder.onBindView(
//                        this,
//                        calendarDate
//                    )
//                }
//            }
//        }
    }

    fun fullBindDisplayRows(
        dateBinder: CalendarDateBinder<ViewHolder>,
        page: CalendarPage
    ) {
        view.calendarPage = page
        view.calendarDateBinder = dateBinder
    }

    companion object {
        fun create(parent: ViewGroup): CalendarPageViewHolder {
            val view = CalendarPageView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            return CalendarPageViewHolder(view)
        }
    }
}
