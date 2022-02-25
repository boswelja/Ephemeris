package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect

/**
 * A lazy pager implementation that has a maximum range of [Int.MAX_VALUE] / 2 before and after the starting
 * position. Theoretically this appears infinite to the user.
 * @param modifier A [Modifier] that is applied to the Pager. Note each page will use the size from this.
 * @param content The page content to display. THe page index is provided, which may be positive or negative.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun InfiniteHorizontalPager(
    modifier: Modifier = Modifier,
    state: InfinitePagerState = rememberInfinitePagerState(),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (page: Int) -> Unit
) {
    val pagerState = rememberPagerState(state.internalStartPage)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            state.page = state.calculatePageFromInternal(it)
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        count = state.pageCount,
        contentPadding = contentPadding,
        key = state::calculatePageFromInternal
    ) { index ->
        // Map the page to start at centered value of 0
        val actualPage = remember { state.calculatePageFromInternal(index) }
        Box(modifier) {
            content(actualPage)
        }
    }
}

abstract class InfinitePagerState {

    abstract val pageCount: Int

    abstract var page: Int
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
fun rememberInfinitePagerState(startPage: Int = 0): InfinitePagerState {
    return remember(startPage) {
        DefaultInfinitePagerState(startPage)
    }
}
