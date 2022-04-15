package com.boswelja.ephemeris.views.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter
import kotlin.math.abs

internal class AlternatingHeightPagerAdapter(
    private vararg val heights: Int
) : InfinitePagerAdapter<ViewHolder>() {

    private val differentHeights = heights.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            View(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        )
    }

    override fun onBindHolder(holder: ViewHolder, page: Int) {
        holder.bind(page)
        holder.itemView.updateLayoutParams {
            height = getHeightFor(page)
        }
    }

    fun getHeightFor(page: Int): Int {
        return heights[abs(page % differentHeights)]
    }
}
