package com.example.myweibo.ui.liquidglass

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class LiquidGlassAppearanceSourceTest {
    private fun readSource(path: String): String =
        listOf(File("src/main/java/$path"), File("app/src/main/java/$path"))
            .first(File::exists)
            .readText()

    @Test
    fun bottomNavigationUsesBlackStrokeAndStrongerShadow() {
        val tabsSource = readSource("com/example/myweibo/ui/liquidglass/LiquidBottomTabs.kt")
        val buttonSource = readSource("com/example/myweibo/ui/liquidglass/LiquidButton.kt")

        assertTrue(buttonSource.contains("Color.Black.copy(alpha = 0.3f)"))
        assertTrue(buttonSource.contains("Stroke(width = 1.dp.toPx())"))
        assertTrue(tabsSource.contains("drawLiquidGlassStroke()"))
        assertTrue(tabsSource.contains("Shadow(alpha = progress * 1.2f)"))
    }

    @Test
    fun liquidIconButtonsUseBlackStrokeAndShadow() {
        val buttonSource = readSource("com/example/myweibo/ui/liquidglass/LiquidButton.kt")

        assertTrue(buttonSource.contains("Shadow(alpha = 0.24f)"))
        assertTrue(buttonSource.contains("Color.Black.copy(alpha = 0.3f)"))
        assertTrue(buttonSource.contains("Stroke(width = 1.dp.toPx())"))
    }
}
