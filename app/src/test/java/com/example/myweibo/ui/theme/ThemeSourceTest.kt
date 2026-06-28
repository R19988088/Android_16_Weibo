package com.example.myweibo.ui.theme

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeSourceTest {
    @Test
    fun themeAppliesCustomAccentToMaterialColorScheme() {
        val source = listOf(
            File("src/main/java/com/example/myweibo/ui/theme/Theme.kt"),
            File("app/src/main/java/com/example/myweibo/ui/theme/Theme.kt"),
        ).first(File::exists).readText()

        assertTrue(source.contains("accentColor: Color? = null"))
        assertTrue(source.contains("primary = it"))
        assertTrue(source.contains("secondary = it"))
        assertTrue(source.contains("tertiary = it"))
    }

    @Test
    fun typographyAddsDefaultTextRoom() {
        val source = listOf(
            File("src/main/java/com/example/myweibo/ui/theme/Type.kt"),
            File("app/src/main/java/com/example/myweibo/ui/theme/Type.kt"),
        ).first(File::exists).readText()

        assertTrue(source.contains("fontSize = fontSize.plusSp(2f)"))
        assertTrue(source.contains("lineHeight = lineHeight.plusSp(3f)"))
        assertTrue(source.contains("bodyLarge = BaseTypography.bodyLarge.defaultTextRoom()"))
    }
}
