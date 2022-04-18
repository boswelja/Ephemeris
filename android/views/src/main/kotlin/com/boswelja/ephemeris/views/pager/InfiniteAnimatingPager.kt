package com.boswelja.ephemeris.views.pager

import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * An implementation of [InfiniteHorizontalPager] that automatically animates page height changes.
 */
public open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteHorizontalPager(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator()

    init {
        itemAnimator = PageChangeAnimator { viewHolder, fromHeight, toHeight ->
            animateHeight(viewHolder, toHeight, fromHeight)
        }
    }

    override fun onPageSnapping(page: Int) {
        super.onPageSnapping(page)
        remeasureAndAnimateHeight(page)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        // Add LayoutTransition to handle page change crossfade
        if (child is ViewGroup) {
            child.layoutTransition = LayoutTransition()
        }
    }

    private fun remeasureAndAnimateHeight(position: Int) {
        val viewHolder = findViewHolderForAdapterPosition(position)
        viewHolder?.itemView?.post {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(viewHolder.itemView.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            viewHolder.itemView.measure(wMeasureSpec, hMeasureSpec)
            val targetHeight = viewHolder.itemView.measuredHeight
            animateHeight(
                viewHolder,
                targetHeight
            )
        }
    }

    private fun animateHeight(
        viewHolder: ViewHolder,
        toHeight: Int,
        fromHeight: Int = height
    ) {
        if (fromHeight != toHeight) {
            val page = positionToPage(viewHolder.bindingAdapterPosition)
            if (page == currentPage) {
                heightAnimator.apply {
                    removeAllUpdateListeners()
                    setIntValues(fromHeight, toHeight)
                    addUpdateListener { animator ->
                        val animatedHeight = animator.animatedValue as Int
                        setHeight(animatedHeight)
                    }
                }
                heightAnimator.start()
            }
        }
    }

    private fun setHeight(targetHeight: Int) {
        for (index in 0 until childCount) {
            getChildAt(index).apply {
                layoutParams = layoutParams.also {
                    it.height = targetHeight
                }
            }
        }
    }
}

internal class PageChangeAnimator(
    private val onAnimateHeight: (viewHolder: ViewHolder, fromHeight: Int, toHeight: Int) -> Unit
) : DefaultItemAnimator() {

    override fun recordPostLayoutInformation(
        state: RecyclerView.State,
        viewHolder: ViewHolder
    ): ItemHolderInfo {
        viewHolder.itemView.apply {
            val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            measure(wMeasureSpec, hMeasureSpec)
        }
        return ItemHolderInfo().apply {
            top = 0
            left = viewHolder.itemView.left
            right = viewHolder.itemView.right
            bottom = viewHolder.itemView.measuredHeight
        }
    }

    override fun animateChange(
        oldHolder: ViewHolder,
        newHolder: ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        val fromHeight = preInfo.bottom - preInfo.top
        val toHeight = postInfo.bottom - postInfo.top
        onAnimateHeight(newHolder, fromHeight, toHeight)
        dispatchAnimationFinished(oldHolder)
        if (oldHolder != newHolder) dispatchAnimationFinished(newHolder)
        return false
    }
}
