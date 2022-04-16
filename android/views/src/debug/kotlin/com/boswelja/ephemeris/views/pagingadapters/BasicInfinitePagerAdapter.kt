package com.boswelja.ephemeris.views.pagingadapters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter

internal class BasicInfinitePagerAdapter : InfinitePagerAdapter<ViewHolder>() {
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
    }
}

public class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    public fun bind(page: Int) {
        val color = if (page % 2 == 0) Color.CYAN else Color.MAGENTA
        itemView.setBackgroundColor(color)
    }
}
