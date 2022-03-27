package com.boswelja.ephemeris.views.pager

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

open class InfiniteAnimatingPager @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : InfiniteHorizontalPager(context, attrs, defStyleAttr) {

    private val heightAnimator = ValueAnimator().apply {
        interpolator = FastOutSlowInInterpolator()
    }

    init {
        itemAnimator = PageChangeAnimator(heightAnimator)
    }

    override fun onPageSnap(page: Int) {
        super.onPageSnap(page)
        //remeasureAndAnimateHeight(page)
    }

    internal fun remeasureAndAnimateHeight(position: Int) {
        val view = findViewHolderForAdapterPosition(position)?.itemView
        view?.post {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (height != view.measuredHeight) {
                heightAnimator.apply {
                    setIntValues(height, view.measuredHeight)
                }.also { it.start() }
            }
        }
    }
}

internal class PageChangeAnimator(
    private val heightAnimator: ValueAnimator
) : DefaultItemAnimator() {

    init {
        heightAnimator.addListener(object : AnimatorListener {
            override fun onAnimationEnd(animator: Animator?) {
                heightAnimator.removeAllUpdateListeners()
            }

            override fun onAnimationCancel(p0: Animator?) {
                heightAnimator.removeAllUpdateListeners()
            }

            override fun onAnimationRepeat(p0: Animator?) { }
            override fun onAnimationStart(p0: Animator?) { }
        })
    }

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
        heightAnimator.setIntValues(fromHeight, toHeight)
        heightAnimator.addUpdateListener { animator ->
            newHolder.itemView.layoutParams = newHolder.itemView.layoutParams.also {
                it.height = animator.animatedValue as Int
            }
        }
        heightAnimator.start()
        return false
    }
}
