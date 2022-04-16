package com.boswelja.ephemeris.views.pager

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.boswelja.ephemeris.views.InfiniteHorizontalPagerFragment
import com.boswelja.ephemeris.views.R
import com.boswelja.ephemeris.views.pagingadapters.BasicInfinitePagerAdapter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

private const val SCROLL_SETTLE_TIME = 500L

@RunWith(AndroidJUnit4::class)
@LargeTest
class InfiniteHorizontalPagerTest {

    @Test
    fun userScrolling_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        // Scroll forwards
        repeat(5) {
            val initialPage = pager.currentPage

            onView(withId(R.id.pager_view)).perform(swipeLeft())
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not greater than $initialPage",
                pager.currentPage > initialPage
            )
        }

        // Scroll backwards
        repeat(5) {
            val initialPage = pager.currentPage

            onView(withId(R.id.pager_view)).perform(swipeRight())
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not less than $initialPage",
                pager.currentPage < initialPage
            )
        }

        // Scroll mixed
        repeat(10) {
            val initialPage = pager.currentPage

            val action = if (it % 2 == 0) swipeLeft() else swipeRight()
            onView(withId(R.id.pager_view)).perform(action)
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertNotEquals(
                pager.currentPage,
                initialPage
            )
        }
    }

    @Test
    fun smoothScrollToPosition_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        // Scroll forwards
        repeat(5) {
            val initialPage = pager.currentPage

            scenario.onFragment {
                it.pager.smoothScrollToPosition(initialPage + 1)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not greater than $initialPage",
                pager.currentPage > initialPage
            )
        }

        // Scroll backwards
        repeat(5) {
            val initialPage = pager.currentPage

            scenario.onFragment {
                it.pager.smoothScrollToPosition(initialPage - 1)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not less than $initialPage",
                pager.currentPage < initialPage
            )
        }

        // Scroll mixed
        repeat(10) { iteration ->
            val initialPage = pager.currentPage

            val offset = if (iteration % 2 == 0) 1 else -1
            scenario.onFragment {
                it.pager.smoothScrollToPosition(initialPage + offset)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertNotEquals(
                pager.currentPage,
                initialPage
            )
        }
    }

    @Test
    fun scrollToPosition_updatesCurrentPage() {
        // Init pager
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        // Scroll forwards
        repeat(5) {
            val initialPage = pager.currentPage

            scenario.onFragment {
                it.pager.scrollToPosition(initialPage + 1)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not greater than $initialPage",
                pager.currentPage > initialPage
            )
        }

        // Scroll backwards
        repeat(5) {
            val initialPage = pager.currentPage

            scenario.onFragment {
                it.pager.scrollToPosition(initialPage - 1)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertTrue(
                "${pager.currentPage} is not less than $initialPage",
                pager.currentPage < initialPage
            )
        }

        // Scroll mixed
        repeat(10) { iteration ->
            val initialPage = pager.currentPage

            val offset = if (iteration % 2 == 0) 1 else -1
            scenario.onFragment {
                it.pager.scrollToPosition(initialPage + offset)
            }
            Thread.sleep(SCROLL_SETTLE_TIME)

            assertNotEquals(
                pager.currentPage,
                initialPage
            )
        }
    }

    @Test
    fun canScrollFarBack() {
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        // Heading back 1000 pages from the start
        val targetPosition = -100000
        scenario.onFragment {
            it.pager.scrollToPosition(targetPosition)
        }
        Thread.sleep(SCROLL_SETTLE_TIME)

        assertEquals(
            targetPosition,
            pager.currentPage
        )
    }

    @Test
    fun canScrollFarForward() {
        val scenario = launchFragmentInContainer<InfiniteHorizontalPagerFragment>()
        val pager = scenario.initAndGetPager()

        // Heading back 1000 pages from the start
        val targetPosition = 100000
        scenario.onFragment {
            it.pager.scrollToPosition(targetPosition)
        }
        Thread.sleep(SCROLL_SETTLE_TIME)

        assertEquals(
            targetPosition,
            pager.currentPage
        )
    }

    private fun FragmentScenario<InfiniteHorizontalPagerFragment>.initAndGetPager(
        adapter: InfinitePagerAdapter<*> = BasicInfinitePagerAdapter()
    ): InfiniteHorizontalPager {
        var pager: InfiniteHorizontalPager? = null
        onFragment {
            pager = it.pager
            it.pager.adapter = adapter
        }
        return pager!!
    }
}
