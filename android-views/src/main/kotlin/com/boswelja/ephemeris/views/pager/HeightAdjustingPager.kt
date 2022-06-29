package com.boswelja.ephemeris.views.pager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * An implementation of [RecyclerView] that provides seemingly infinite left/right paging support.
 * Implementations of this must subclass [InfinitePagerAdapter] for their adapter.
 */
internal class HeightAdjustingPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var snapPositionChangeListener: ((page: Int) -> Unit)? = null
    private val snapHelper = PagerSnapHelper()

    /**
     * The current page that the pager is snapped to.
     */
    var currentPage: Int = 0
        private set

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        // Disable item change animations, otherwise we get a crossfade when changing a page
        (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    /**
     * Called when the pager is snapping to a new page.
     */
    private fun onPageSnapping(page: Int) {
        snapPositionChangeListener?.invoke(page)
    }

    fun setOnSnapPositionChangeListener(listener: (page: Int) -> Unit) {
        snapPositionChangeListener = listener
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (scrollState != SCROLL_STATE_DRAGGING) {
            val snapTarget = snapHelper.findTargetSnapPosition(layoutManager, dx, dy)
            if (snapTarget != NO_POSITION) {
                val snapPage = positionToPage(snapTarget)
                maybeNotifySnapPositionChange(snapPage)
            }
        }
    }

    override fun smoothScrollToPosition(position: Int) {
        super.smoothScrollToPosition(pageToPosition(position))
    }

    override fun scrollToPosition(position: Int) {
        super.scrollToPosition(pageToPosition(position))
        // Since scrollToPosition doesn't actually trigger a scroll state change, we need to manually
        // notify listeners. Maybe there's a better way of handling this?
        maybeNotifySnapPositionChange(position)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        super.scrollToPosition(pageToPosition(currentPage))
    }

    private fun maybeNotifySnapPositionChange(snapPosition: Int) {
        val snapPositionChanged = currentPage != snapPosition
        if (snapPositionChanged) {
            currentPage = snapPosition
            updateHeight()
            onPageSnapping(snapPosition)
        }
    }

    private fun updateHeight() {
        val viewHolder = findViewHolderForLayoutPosition(pageToPosition(currentPage))
        if (viewHolder == null) {
            Log.w("HeightAdjustingPager", "Failed to get the child view holder at page $currentPage")
            return
        }

        // Measure the child
        val childHeight = viewHolder.itemView.measureForUnboundedHeight()

        // If child height is higher than parent, set child height to its layout params to make sure
        // it will invalidate properly and not cut
        if (childHeight > measuredHeight) {
            viewHolder.itemView.setHeight(childHeight)
        }

        // Update parent height according to child height
        setHeight(childHeight)
    }

    /**
     * Maps a position from the underlying RecyclerView to a pager page.
     */
    private fun positionToPage(position: Int): Int {
        return (adapter as InfinitePagerAdapter).positionToPage(position)
    }

    /**
     * Maps a pager page to the underlying RecyclerView position
     */
    private fun pageToPosition(page: Int): Int {
        return (adapter as InfinitePagerAdapter).pageToPosition(page)
    }
}

private fun View.measureForUnboundedHeight(): Int {
    measure(
        MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
    return measuredHeight
}

private fun View.setHeight(newHeight: Int) {
    layoutParams = layoutParams.apply {
        height = newHeight
    }
}
