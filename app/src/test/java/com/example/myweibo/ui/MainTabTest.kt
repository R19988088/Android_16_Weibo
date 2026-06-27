package com.example.myweibo.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MainTabTest {
    @Test
    fun primaryTabsExcludeCompose() {
        assertEquals(
            listOf(MainTab.Feed, MainTab.Messages, MainTab.Search, MainTab.Mine),
            MainTab.primaryTabs,
        )
        assertFalse(MainTab.Compose in MainTab.primaryTabs)
    }
}
