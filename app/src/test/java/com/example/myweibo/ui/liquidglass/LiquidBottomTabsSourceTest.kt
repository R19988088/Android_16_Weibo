package com.example.myweibo.ui.liquidglass

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LiquidBottomTabsSourceTest {
    @Test
    fun selectedTabIndexIsResolvedFromCompositionBeforeAnimationSync() {
        val source = listOf(
            File("src/main/java/com/example/myweibo/ui/liquidglass/LiquidBottomTabs.kt"),
            File("app/src/main/java/com/example/myweibo/ui/liquidglass/LiquidBottomTabs.kt"),
        ).first(File::exists).readText()

        assertTrue(source.contains("val resolvedSelectedTabIndex = selectedTabIndex()"))
        assertTrue(source.contains("snapshotFlow { isUserGesturing to resolvedSelectedTabIndex }"))
        assertFalse(source.contains("snapshotFlow { isUserGesturing to selectedTabIndex() }"))
    }
}
