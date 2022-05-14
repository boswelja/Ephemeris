package com.boswelja.ephemeris.views.pagingadapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.boswelja.ephemeris.views.databinding.ItemVaryingHeightBinding
import com.boswelja.ephemeris.views.pager.InfinitePagerAdapter
import kotlin.math.abs

internal class AlternatingHeightPagerAdapter(
    private vararg val heights: Int
) : InfinitePagerAdapter<VaryingHeightViewHolder>() {

    private val differentHeights = heights.size

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaryingHeightViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VaryingHeightViewHolder(
            ItemVaryingHeightBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VaryingHeightViewHolder, position: Int) {
        val page = positionToPage(position)
        val color = if (page % 2 == 0) Color.CYAN else Color.MAGENTA
        holder.binding.content.apply {
            setBackgroundColor(color)
            updateLayoutParams {
                height = getHeightFor(page)
            }
        }
    }

    fun getHeightFor(page: Int): Int {
        return heights[abs(page % differentHeights)]
    }
}

internal class VaryingHeightViewHolder(
    val binding: ItemVaryingHeightBinding
) : RecyclerView.ViewHolder(binding.root)
