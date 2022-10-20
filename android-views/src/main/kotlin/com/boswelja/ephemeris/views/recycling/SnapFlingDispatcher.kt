package com.boswelja.ephemeris.views.recycling

import android.view.animation.DecelerateInterpolator
import kotlin.time.Duration

public class SnapFlingDispatcher(
    private val maxFlingDuration: Duration,
    private val maxFlingDistance: Int
) : FlingDispatcher {
    private val interpolator = DecelerateInterpolator()
    private var flingStartTimeMs: Long? = null
    private var flingFinishTimeMs: Long? = null

    override var deltaX: Float = 0f
        private set
    override var deltaY: Float = 0f
        private set

    override fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int) {
        TODO("Not yet implemented")
    }

    override fun abortFlingInProgress() {
        TODO("Not yet implemented")
    }

    override fun computeScrollDelta(): Boolean {
        val fraction = computeAnimationFraction()
        if (fraction < 0f) return false
        val interpolation = interpolator.getInterpolation(fraction)
        return true
    }

    private fun computeAnimationFraction(): Float {
        if (flingStartTimeMs == null || flingFinishTimeMs == null) return -1f
        val timeNow = System.currentTimeMillis()
        if (timeNow > flingFinishTimeMs!!) return -1f
        return (timeNow - flingStartTimeMs!!) / (flingFinishTimeMs!! - flingStartTimeMs!!).toFloat()
    }
}