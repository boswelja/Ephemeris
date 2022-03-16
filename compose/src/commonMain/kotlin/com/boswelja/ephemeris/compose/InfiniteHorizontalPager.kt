package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot

@Composable
public fun InfiniteHorizontalPager(
    modifier: Modifier = Modifier,
    state: InfinitePagerState = rememberInfinitePagerState(),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (page: Int) -> Unit
) {
    val internalLazyState = rememberLazyListState(initialFirstVisibleItemIndex = state.internalStartPage)
    LaunchedEffect(internalLazyState) {
        snapshotFlow { internalLazyState.isScrollInProgress }
            .filterNot { it }
            .collect {
                val itemToSnap = if (internalLazyState.firstVisibleItemScrollOffset > internalLazyState.layoutInfo.viewportEndOffset / 2) {
                    internalLazyState.firstVisibleItemIndex + 1
                } else {
                    internalLazyState.firstVisibleItemIndex
                }
                internalLazyState.animateScrollToItem(itemToSnap)
            }
    }
    LazyRow(
        modifier = modifier,
        state = internalLazyState,
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
