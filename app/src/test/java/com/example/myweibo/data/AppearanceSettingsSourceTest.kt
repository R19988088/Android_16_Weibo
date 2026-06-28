package com.example.myweibo.data

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class AppearanceSettingsSourceTest {
    @Test
    fun appearanceSettingsPersistCustomAccentColor() {
        val source = listOf(
            File("src/main/java/com/example/myweibo/data/AppearanceSettingsStore.kt"),
            File("app/src/main/java/com/example/myweibo/data/AppearanceSettingsStore.kt"),
        ).first(File::exists).readText()

        assertTrue(source.contains("fun readAccentColorArgb(): Long?"))
        assertTrue(source.contains("fun writeAccentColorArgb(argb: Long?)"))
        assertTrue(source.contains("KEY_ACCENT_COLOR_ARGB"))
    }
}
