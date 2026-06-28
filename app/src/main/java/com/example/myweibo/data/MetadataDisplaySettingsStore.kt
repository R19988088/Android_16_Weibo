package com.example.myweibo.data

import android.content.Context

data class MetadataDisplaySettings(
    val showDeviceSource: Boolean = false,
    val customLocation: String = "",
)

class MetadataDisplaySettingsStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun read(): MetadataDisplaySettings =
        MetadataDisplaySettings(
            showDeviceSource = prefs.getBoolean(KEY_SHOW_DEVICE_SOURCE, false),
            customLocation = prefs.getString(KEY_CUSTOM_LOCATION, "").orEmpty(),
        )

    fun write(settings: MetadataDisplaySettings) {
        prefs.edit()
            .putBoolean(KEY_SHOW_DEVICE_SOURCE, settings.showDeviceSource)
            .putString(KEY_CUSTOM_LOCATION, settings.customLocation)
            .apply()
    }

    private companion object {
        const val PREFS_NAME = "weibo_app_prefs"
        const val KEY_SHOW_DEVICE_SOURCE = "metadata_show_device_source"
        const val KEY_CUSTOM_LOCATION = "metadata_custom_location"
    }
}
