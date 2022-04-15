package com.boswelja.ephemeris.views.pager

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.boswelja.ephemeris.views.InfiniteAnimatingPagerFragment
import com.boswelja.ephemeris.views.R
import com.boswelja.ephemeris.views.pagingadapters.AlternatingHeightPagerAdapter
import com.boswelja.ephemeris.views.pagingadapters.BasicInfinitePagerAdapter
import org.junit.Assert.assertEquals
import org.junit.Test

private const val ANIMATION_TIMEOUT = 400L

class InfiniteAnimatingPagerTest {

    @Test
    fun initialViewHeight_isCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )
        Thread.sleep(ANIMATION_TIMEOUT)

        val expectedHeight = adapter.getHeightFor(pager.currentPage)
        assertEquals(
            expectedHeight,
            pager.height
        )
    }

    @Test
    fun onUserScrolled_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )
        Thread.sleep(ANIMATION_TIMEOUT)

        // Scroll forwards
        repeat(5) {
            onView(withId(R.id.pager_view)).perform(swipeLeft())
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll backwards
        repeat(5) {
            onView(withId(R.id.pager_view)).perform(swipeRight())
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll mixed
        repeat(10) {
            val action = if (it % 2 == 0) swipeLeft() else swipeRight()
            onView(withId(R.id.pager_view)).perform(action)
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }
    }

    @Test
    fun scrollToPosition_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )
        Thread.sleep(ANIMATION_TIMEOUT)

        // Scroll forwards
        repeat(5) {
            scenario.onFragment {
                it.pager.scrollToPosition(it.pager.currentPage + 1)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll backwards
        repeat(5) {
            scenario.onFragment {
                it.pager.scrollToPosition(it.pager.currentPage - 1)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll mixed
        repeat(10) { iteration ->
            val offset = if (iteration % 2 == 0) 1 else -1
            scenario.onFragment {
                it.pager.scrollToPosition(it.pager.currentPage - offset)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }
    }

    @Test
    fun smoothScrollToPosition_viewHeightIsCorrect() {
        val adapter = AlternatingHeightPagerAdapter(700, 500, 300)
        val scenario = launchFragmentInContainer<InfiniteAnimatingPagerFragment>()
        val pager = scenario.initAndGetPager(
            adapter = adapter
        )
        Thread.sleep(ANIMATION_TIMEOUT)

        // Scroll forwards
        repeat(5) {
            scenario.onFragment {
                it.pager.smoothScrollToPosition(it.pager.currentPage + 1)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll backwards
        repeat(5) {
            scenario.onFragment {
                it.pager.smoothScrollToPosition(it.pager.currentPage - 1)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }

        // Scroll mixed
        repeat(10) { iteration ->
            val offset = if (iteration % 2 == 0) 1 else -1
            scenario.onFragment {
                it.pager.smoothScrollToPosition(it.pager.currentPage - offset)
            }
            Thread.sleep(ANIMATION_TIMEOUT)

            assertEquals(
                adapter.getHeightFor(pager.currentPage),
                pager.height
            )
        }
    }

    private fun FragmentScenario<InfiniteAnimatingPagerFragment>.initAndGetPager(
        adapter: InfinitePagerAdapter<*> = BasicInfinitePagerAdapter()
    ): InfiniteAnimatingPager {
        var pager: InfiniteAnimatingPager? = null
        onFragment {
            pager = it.pager
            it.pager.adapter = adapter
        }
        return pager!!
    }
}
