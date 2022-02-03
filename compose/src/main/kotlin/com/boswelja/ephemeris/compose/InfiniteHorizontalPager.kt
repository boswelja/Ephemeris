package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot

/**
 * A lazy pager implementation that has a maximum range of [Int.MAX_VALUE] / 2 before and after the starting
 * position. Theoretically this appears infinite to the user.
 * @param modifier A [Modifier] that is applied to the Pager. Note each page will use the size from this.
 * @param content The page content to display. THe page index is provided, which may be positive or negative.
 */
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun InfiniteHorizontalPager(
    modifier: Modifier = Modifier,
    content: @Composable (page: Int) -> Unit
) {
    val centerOffset = Int.MAX_VALUE / 2
    val state = rememberLazyListState()
    LaunchedEffect(state) {
        // Scroll to the middle
        state.scrollToItem(centerOffset)
    }
    LaunchedEffect(key1 = state) {
        val snapOffsetThreshold = state.layoutInfo.viewportEndOffset / 2
        snapshotFlow { state.isScrollInProgress }
            .filterNot { it }
            .collect {
                // Smooth scroll to the "nearest" item
                val itemIndex = if (state.firstVisibleItemScrollOffset < snapOffsetThreshold) {
                    state.firstVisibleItemIndex
                } else {
                    state.firstVisibleItemIndex + 1
                }
                state.animateScrollToItem(itemIndex)
            }
    }

    LazyRow(
        modifier = modifier,
        state = state
    ) {
        items(
            count = Int.MAX_VALUE,
            key = { it - centerOffset }
        ) { index ->
            // Map the page to start at centered value of 0
            val actualPage = remember(index) { index - centerOffset }
            Box(modifier) {
                content(actualPage)
            }
        }
    }
}
