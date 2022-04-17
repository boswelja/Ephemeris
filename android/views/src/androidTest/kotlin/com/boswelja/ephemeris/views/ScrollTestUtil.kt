package com.boswelja.ephemeris.views

import kotlin.random.Random

/**
 * A utility function that performs multiple scroll events, both sequential and random scrolls.
 * @param onScroll Called for each iteration of scroll tests. Both iteration and direction are provided.
 * @param onResetState Called in between scroll loops. This should be used to reset any state.
 * @param sequentialIterations The number of iterations for sequential scroll loops. A sequential loop
 * will call [onScroll] for the given number of iterations with the same direction each time.
 * @param randomIterations The number of iterations for random scroll loops. A random loop will call
 * [onScroll] for the given number of iterations with a randomly chosen direction each time.
 */
fun testWithScrolling(
    onScroll: (iteration: Int, direction: Direction) -> Unit,
    onResetState: (() -> Unit)? = null,
    sequentialIterations: Int = 3,
    randomIterations: Int = 5
) {
    // Scroll left to right
    repeat(sequentialIterations) { iteration ->
        onScroll(iteration, Direction.LeftToRight)
    }

    onResetState?.invoke()

    // Scroll right to left
    repeat(sequentialIterations) { iteration ->
        onScroll(iteration, Direction.RightToLeft)
    }

    onResetState?.invoke()

    // Scroll randomly
    val directions = Direction.values()
    repeat(randomIterations) { iteration ->
        val direction = directions[Random.nextInt(until = directions.size)]
        onScroll(iteration, direction)
    }
}

enum class Direction {
    LeftToRight,
    RightToLeft
}
