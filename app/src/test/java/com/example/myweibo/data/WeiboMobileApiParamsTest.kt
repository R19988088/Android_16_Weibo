package com.example.myweibo.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class WeiboMobileApiParamsTest {
    @Test
    fun parsesQueryStringAndNewlineParamsInOrder() {
        val params = WeiboMobileApiParams.parse(
            """
            source=abc
            gsid=token-1&check=token-2
            empty=
            """.trimIndent(),
        )

        assertEquals(listOf("source", "gsid", "check"), params.keys.toList())
        assertEquals("abc", params["source"])
        assertEquals("token-1", params["gsid"])
        assertEquals("token-2", params["check"])
        assertFalse(params.containsKey("empty"))
    }
}
