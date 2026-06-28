package com.example.myweibo.data

internal object WeiboMobileApiParams {
    fun parse(raw: String): LinkedHashMap<String, String> {
        val params = linkedMapOf<String, String>()
        raw.split('&', '\n')
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .forEach { part ->
                val key = part.substringBefore('=', "").trim()
                val value = part.substringAfter('=', "").trim()
                if (key.isNotBlank() && value.isNotBlank()) {
                    params[key] = value
                }
            }
        return params
    }
}
