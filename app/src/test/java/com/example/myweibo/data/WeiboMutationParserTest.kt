package com.example.myweibo.data

import org.junit.Assert.assertTrue
import org.junit.Test

class WeiboMutationParserTest {
    @Test
    fun mobileApiCodeSuccessIsAccepted() {
        WeiboJsonParser.assertMutationSuccess("""{"code":"100000","data":{"id":"1"}}""", "failed")
    }

    @Test
    fun mobileApiMessageIsUsedForFailure() {
        val result = runCatching {
            WeiboJsonParser.assertMutationSuccess("""{"ok":0,"message":"bad request"}""", "failed")
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("bad request"))
    }
}
