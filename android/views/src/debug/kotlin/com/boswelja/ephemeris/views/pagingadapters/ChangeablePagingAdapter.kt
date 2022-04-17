package com.boswelja.ephemeris.views.pagingadapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter

internal class ChangeablePagingAdapter(
    private val initialHeight: Int,
    private val changedHeight: Int
) : InfinitePagerAdapter<ViewHolder>() {

    private var useChangedHeight: Boolean = false

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
            height = if (useChangedHeight) changedHeight else initialHeight
        }
    }

    fun changeItem(page: Int, useChangedHeight: Boolean) {
        this.useChangedHeight = useChangedHeight
        notifyItemChanged(page + (Int.MAX_VALUE / 2))
    }
}
