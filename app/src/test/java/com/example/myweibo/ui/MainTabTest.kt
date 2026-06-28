package com.example.myweibo.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MainTabTest {
    @Test
    fun primaryTabsOnlyIncludeBottomNavigationPages() {
        assertEquals(
            listOf(MainTab.Feed, MainTab.Messages, MainTab.Mine),
            MainTab.primaryTabs,
        )
        assertFalse(MainTab.Search in MainTab.primaryTabs)
        assertFalse(MainTab.Compose in MainTab.primaryTabs)
    }

    @Test
    fun profilePagerSideEffectsOnlyRunForCurrentPage() {
        assertEquals(
            setOf(ProfilePagerSideEffect.PostsLoadMore),
            activeProfilePagerSideEffects(MineContentTab.Posts.ordinal),
        )
        assertEquals(
            setOf(ProfilePagerSideEffect.AlbumLoadMore, ProfilePagerSideEffect.AlbumPrefetch),
            activeProfilePagerSideEffects(MineContentTab.Album.ordinal),
        )
    }
}
