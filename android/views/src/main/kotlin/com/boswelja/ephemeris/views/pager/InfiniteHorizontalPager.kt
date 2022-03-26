package com.boswelja.ephemeris.views.pager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class InfiniteHorizontalPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapHelper = PagerSnapHelper()

    private var snapPosition: Int = 0

    var snapPositionChangeListener: OnSnapPositionChangeListener? = null

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        require(adapter is InfinitePagerAdapter<*>)
        super.setAdapter(adapter)
    }

    fun setAdapter(adapter: InfinitePagerAdapter<*>) = setAdapter(adapter as Adapter<*>)

    private fun maybeNotifySnapPositionChange() {
        snapPositionChangeListener?.let {
            val snapPosition = positionToPage(snapHelper.getSnapPosition(this))
            val snapPositionChanged = this.snapPosition != snapPosition
            if (snapPositionChanged) {
                it.onSnapPositionChange(snapPosition)
                this.snapPosition = snapPosition
            }
        }
    }

    private fun positionToPage(position: Int): Int {
        return position - (MAX_PAGES / 2)
    }

    companion object {
        private const val MAX_PAGES = Int.MAX_VALUE
    }
}
