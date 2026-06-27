package com.example.myweibo.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MainTabTest {
    @Test
    fun primaryTabsExcludeSearchAndCompose() {
        assertEquals(
            listOf(MainTab.Feed, MainTab.Messages, MainTab.Mine),
            MainTab.primaryTabs,
        )
        assertFalse(MainTab.Search in MainTab.primaryTabs)
        assertFalse(MainTab.Compose in MainTab.primaryTabs)
    }
}
