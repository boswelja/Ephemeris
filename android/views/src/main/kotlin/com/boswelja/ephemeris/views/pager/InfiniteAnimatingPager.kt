package com.boswelja.ephemeris.views.pager

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * An implementation of [InfiniteHorizontalPager] that automatically animates page height changes.
 */
public open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteHorizontalPager(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator()

    init {
        itemAnimator = PageChangeAnimator(heightAnimator)
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
        toHeight: Int
    ) {
        if (height != toHeight) {
            val page = positionToPage(viewHolder.bindingAdapterPosition)
            // TODO This assumes there are 3 views, not good
            val prevView = findViewHolderForAdapterPosition(page - 1)?.itemView
            val nextView = findViewHolderForAdapterPosition(page + 1)?.itemView
            heightAnimator.apply {
                removeAllUpdateListeners()
                setIntValues(height, toHeight)
                addUpdateListener { animator ->
                    prevView?.layoutParams = prevView!!.layoutParams.also {
                        it.height = animator.animatedValue as Int
                    }
                    nextView?.layoutParams = nextView!!.layoutParams.also {
                        it.height = animator.animatedValue as Int
                    }
                    viewHolder.itemView.apply {
                        layoutParams = layoutParams.also {
                            it.height = animator.animatedValue as Int
                        }
                    }
                }
            }
            heightAnimator.start()
        }
    }
}

internal class PageChangeAnimator(
    private val heightAnimator: ValueAnimator
) : DefaultItemAnimator() {

    override fun recordPostLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder
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
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        // TODO This is responsible for incorrect heights when changing page sources. Since it doesn't
        // consider the current page and can therefore animate the height to match the wrong page.
        val fromHeight = preInfo.bottom - preInfo.top
        val toHeight = postInfo.bottom - postInfo.top
        if (fromHeight != toHeight) {
            heightAnimator.setIntValues(fromHeight, toHeight)
            heightAnimator.addUpdateListener { animator ->
                newHolder.itemView.layoutParams = newHolder.itemView.layoutParams.also {
                    it.height = animator.animatedValue as Int
                }
            }
            heightAnimator.addListener(
                object : AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                        // No-op
                    }
                    override fun onAnimationRepeat(p0: Animator?) {
                        // No-op
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        heightAnimator.removeAllListeners()
                        heightAnimator.removeAllUpdateListeners()
                        dispatchAnimationFinished(oldHolder)
                        if (oldHolder != newHolder) dispatchAnimationFinished(newHolder)
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                        heightAnimator.removeAllListeners()
                        heightAnimator.removeAllUpdateListeners()
                        dispatchAnimationFinished(oldHolder)
                        if (oldHolder != newHolder) dispatchAnimationFinished(newHolder)
                    }

                }
            )
            heightAnimator.start()
        } else {
            dispatchAnimationFinished(oldHolder)
            if (oldHolder != newHolder) dispatchAnimationFinished(newHolder)
        }
        return false
    }
}
