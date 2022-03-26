package com.boswelja.ephemeris.views.pager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

open class InfiniteHorizontalPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapPositionChangeListeners = mutableListOf<OnSnapPositionChangeListener>()

    private val snapHelper = PagerSnapHelper()

    var currentPage: Int = 0

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        scrollToPosition(currentPage)
    }

    override fun onScrollStateChanged(state: Int) {
        if (state == SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        require(adapter is InfinitePagerAdapter<*>) { "$adapter is not an InfinitePagerAdapter!" }
        super.setAdapter(adapter)
    }

    override fun findViewHolderForAdapterPosition(position: Int): ViewHolder? {
        return super.findViewHolderForAdapterPosition(pageToPosition(position))
    }

    final override fun smoothScrollToPosition(position: Int) {
        super.smoothScrollToPosition(pageToPosition(position))
    }

    final override fun scrollToPosition(position: Int) {
        super.scrollToPosition(pageToPosition(position))
    }

    fun registerSnapPositionChangeListener(
        snapPositionChangeListener: OnSnapPositionChangeListener
    ) {
        snapPositionChangeListeners.add(snapPositionChangeListener)
    }

    fun unregisterSnapPositionChangeListener(
        snapPositionChangeListener: OnSnapPositionChangeListener
    ) {
        snapPositionChangeListeners.remove(snapPositionChangeListener)
    }

    private fun maybeNotifySnapPositionChange() {
        if (snapPositionChangeListeners.isNotEmpty()) {
            val snapPosition = positionToPage(snapHelper.getSnapPosition(this))
            val snapPositionChanged = this.currentPage != snapPosition
            if (snapPositionChanged) {
                snapPositionChangeListeners.onEach {
                    it.onSnapPositionChange(snapPosition)
                }
                this.currentPage = snapPosition
            }
        }
    }

    private fun positionToPage(position: Int): Int {
        return position - (MAX_PAGES / 2)
    }

    private fun pageToPosition(page: Int): Int {
        return page + (Int.MAX_VALUE / 2)
    }

    companion object {
        private const val MAX_PAGES = Int.MAX_VALUE
    }
}
