package com.boswelja.ephemeris.views.recycling

public interface FlingDispatcher {

    public val deltaX: Float
    public val deltaY: Float

    public fun fling(
        startX: Int,
        startY: Int,
        velocityX: Int,
        velocityY: Int
    )

    public fun abortFlingInProgress()

    public fun computeScrollDelta(): Boolean
}