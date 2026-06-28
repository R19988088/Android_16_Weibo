package com.example.myweibo.data

import android.content.Context

enum class AppThemeMode(
    val storageValue: String,
    val label: String,
    val description: String,
) {
    Light("light", "浅色模式", "始终使用浅色外观"),
    Dark("dark", "深色模式", "始终使用深色外观"),
    ;

    companion object {
        fun fromStorage(value: String?): AppThemeMode =
            entries.firstOrNull { it.storageValue == value } ?: Light
    }
}

class AppearanceSettingsStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun readThemeMode(): AppThemeMode =
        AppThemeMode.fromStorage(prefs.getString(KEY_THEME_MODE, null))

    fun readAccentColorArgb(): Long? =
        if (prefs.contains(KEY_ACCENT_COLOR_ARGB)) {
            prefs.getLong(KEY_ACCENT_COLOR_ARGB, 0L)
        } else {
            null
        }

    fun writeThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.storageValue).apply()
    }

    fun writeAccentColorArgb(argb: Long?) {
        prefs.edit().apply {
            if (argb == null) {
                remove(KEY_ACCENT_COLOR_ARGB)
            } else {
                putLong(KEY_ACCENT_COLOR_ARGB, argb)
            }
        }.apply()
    }

    private companion object {
        const val PREFS_NAME = "weibo_app_prefs"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_ACCENT_COLOR_ARGB = "accent_color_argb"
    }
}
