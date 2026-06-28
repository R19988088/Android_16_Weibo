package com.example.myweibo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.myweibo.data.AppearanceSettingsStore
import com.example.myweibo.data.AppThemeMode
import com.example.myweibo.ui.WeiboApp
import com.example.myweibo.ui.theme.MyWeiboTheme
import java.net.CookieHandler
import java.net.CookieManager
import java.net.URI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookieHandler.setDefault(object : CookieHandler() {
            private val fallback = CookieManager()
            override fun get(uri: URI, requestHeaders: Map<String, List<String>>?): Map<String, List<String>> {
                val map = HashMap(fallback.get(uri, requestHeaders))
                val cookieStr = android.webkit.CookieManager.getInstance().getCookie(uri.toString())
                if (!cookieStr.isNullOrBlank()) {
                    val cookies = map.getOrDefault("Cookie", mutableListOf())
                    cookies.addAll(cookieStr.split(";").map { it.trim() })
                    map["Cookie"] = cookies
                }
                return map
            }
            override fun put(uri: URI, responseHeaders: Map<String, List<String>>?) {
                fallback.put(uri, responseHeaders)
                val webkit = android.webkit.CookieManager.getInstance()
                responseHeaders?.forEach { (key, values) ->
                    if (key.equals("Set-Cookie", ignoreCase = true) ||
                        key.equals("Set-Cookie2", ignoreCase = true)
                    ) {
                        values.forEach { cookieLine ->
                            webkit.setCookie(uri.toString(), cookieLine)
                        }
                    }
                }
                webkit.flush()
            }
        })

        enableEdgeToEdge()
        setContent {
            val appearanceSettingsStore = remember { AppearanceSettingsStore(this@MainActivity) }
            var themeMode by remember { mutableStateOf(appearanceSettingsStore.readThemeMode()) }
            var accentColorArgb by remember { mutableStateOf(appearanceSettingsStore.readAccentColorArgb()) }
            val accentColor = accentColorArgb?.let { Color(it.toInt()) }
            MyWeiboTheme(
                darkTheme = themeMode == AppThemeMode.Dark,
                accentColor = accentColor,
            ) {
                WeiboApp(
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        themeMode = mode
                        appearanceSettingsStore.writeThemeMode(mode)
                    },
                    accentColorArgb = accentColorArgb,
                    onAccentColorChange = { argb ->
                        accentColorArgb = argb
                        appearanceSettingsStore.writeAccentColorArgb(argb)
                    },
                )
            }
        }
    }
}
