package com.boswelja.ephemeris.compose

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.LayoutDirection
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

@OptIn(ExperimentalSnapperApi::class)
@Composable
public fun InfiniteHorizontalPager(
    modifier: Modifier = Modifier,
    state: InfinitePagerState = rememberInfinitePagerState(),
    contentPadding: PaddingValues = PaddingValues(),
    maxItemFling: Int = 1,
    content: @Composable (page: Int) -> Unit
) {
    val internalLazyState = rememberLazyListState(initialFirstVisibleItemIndex = state.internalStartPage)
    val layoutInfo = rememberLazyListSnapperLayoutInfo(
        lazyListState = internalLazyState,
        endContentPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
    )

    // Update the pager state page
    LaunchedEffect(internalLazyState.isScrollInProgress) {
        if (!internalLazyState.isScrollInProgress) {
            state.page = layoutInfo.currentItem?.index ?: state.internalStartPage
        }
    }

    var pagerHeight by remember { mutableStateOf(0) }

    LazyRow(
        modifier = modifier,
        state = internalLazyState,
        flingBehavior = rememberSnapperFlingBehavior(
            layoutInfo = layoutInfo,
            snapIndex = { _, startIndex, targetIndex ->
                targetIndex.coerceIn(startIndex - maxItemFling, startIndex + maxItemFling)
            }
        ),
        contentPadding = contentPadding
    ) {
        items(
            count = state.pageCount,
            key = state::calculatePageFromInternal
        ) { index ->
            var pageHeight by remember { mutableStateOf(0) }
            Box(
                Modifier
                    .fillParentMaxWidth()
                    .wrapContentHeight()
                    .layout { measurable, constraints ->
                        // We do our own measurement here so we can change the pager height according
                        // to the current page
                        val placeable = measurable.measure(constraints)
                        pageHeight = placeable.height
                        Log.d("InfiniteHorizontalPager", "Page $index $pageHeight")
                        layout(constraints.maxWidth, pagerHeight) {
                            placeable.placeRelative(0, 0)
                        }
                    }
                    .onGloballyPositioned {
                        // Make sure to update the pager height when a new page is snapped
                        if (it.positionInParent().x == 0f) {
                            pagerHeight = pageHeight
                        }
                    }
            ) {
                content(state.calculatePageFromInternal(index))
            }
        }
    }
}

public abstract class InfinitePagerState {

    public abstract val pageCount: Int

    public abstract var page: Int
        internal set

    internal abstract val internalStartPage: Int

    internal abstract fun calculatePageFromInternal(internalPage: Int): Int
}

internal class DefaultInfinitePagerState(
    startPage: Int
) : InfinitePagerState() {

    override val internalStartPage: Int = Int.MAX_VALUE / 2

    override val pageCount: Int = Int.MAX_VALUE

    override var page: Int by mutableStateOf(startPage)

    override fun calculatePageFromInternal(internalPage: Int): Int {
        return internalPage - internalStartPage
    }
}

@Composable
public fun rememberInfinitePagerState(startPage: Int = 0): InfinitePagerState {
    return remember(startPage) {
        DefaultInfinitePagerState(startPage)
    }
}
