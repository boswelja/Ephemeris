package com.boswelja.ephemeris.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
public fun InfiniteHorizontalPager(
    modifier: Modifier = Modifier,
    state: InfinitePagerState = rememberInfinitePagerState(),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (page: Int) -> Unit
) {
    // TODO
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
