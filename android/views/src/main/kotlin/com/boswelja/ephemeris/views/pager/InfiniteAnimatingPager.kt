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

public open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteHorizontalPager(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator()

    init {
        itemAnimator = PageChangeAnimator(heightAnimator)
    }

    override fun onPageSnap(page: Int) {
        super.onPageSnap(page)
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
        // TODO This assumes there are 3 views, not good
        val view = findViewHolderForAdapterPosition(position)?.itemView
        view?.post {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)
            if (view.height != view.measuredHeight) {
                val prevView = findViewHolderForAdapterPosition(position - 1)?.itemView
                val nextView = findViewHolderForAdapterPosition(position + 1)?.itemView
                heightAnimator.apply {
                    setIntValues(view.height, view.measuredHeight)
                    addUpdateListener { animator ->
                        prevView?.layoutParams = prevView!!.layoutParams.also {
                            it.height = animator.animatedValue as Int
                        }
                        nextView?.layoutParams = nextView!!.layoutParams.also {
                            it.height = animator.animatedValue as Int
                        }
                        view.layoutParams = view.layoutParams.also {
                            it.height = animator.animatedValue as Int
                        }
                    }
                }
                heightAnimator.start()
            }
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
        val fromHeight = preInfo.bottom - preInfo.top
        val toHeight = postInfo.bottom - preInfo.top
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
