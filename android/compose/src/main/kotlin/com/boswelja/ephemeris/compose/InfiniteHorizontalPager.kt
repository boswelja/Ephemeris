package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import dev.chrisbanes.snapper.ExperimentalSnapperApi
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

    LazyRow(
        modifier = modifier,
        state = internalLazyState,
        flingBehavior = rememberSnapperFlingBehavior(
            lazyListState = internalLazyState,
            endContentPadding = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
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
            Box(
                Modifier
                    .fillParentMaxWidth()
                    .wrapContentHeight()
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
