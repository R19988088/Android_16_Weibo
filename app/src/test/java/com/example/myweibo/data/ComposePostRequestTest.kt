package com.example.myweibo.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ComposePostRequestTest {
    @Test
    fun statusParamsIncludeTextPicturesAndVisibility() {
        val params = ComposePostRequest(
            text = "hello",
            picIds = listOf("p1", "p2"),
            visibility = ComposeVisibility.Friends,
        ).toWebParams()

        assertEquals("hello", params["content"])
        assertEquals("p1,p2", params["pic_id"])
        assertEquals("6", params["visible"])
        assertEquals("1", params["sync_mblog"])
        assertEquals("", params["topic_id"])
        assertFalse(params.containsKey("video_id"))
    }

    @Test
    fun emptyStatusRequestIsRejectedBeforeNetwork() {
        val result = runCatching { ComposePostRequest(text = "  ").requireValid() }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("内容不能为空"))
    }

    @Test
    fun uploadedImagesBecomePicIdParams() {
        val params = listOf(
            ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-a", "pic_id"),
            ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-b", "pic_id"),
        ).toPostRequest(text = "with images").toWebParams()

        assertEquals("pic-a,pic-b", params["pic_id"])
        assertFalse(params.containsKey("video_id"))
    }

    @Test
    fun uploadedVideoKeepsPublishParamName() {
        val params = listOf(
            ComposeMediaUploadResult(ComposeMediaKind.Video, "media-a", "media_id"),
        ).toPostRequest(text = "with video").toWebParams()

        assertEquals("media-a", params["media_id"])
        assertFalse(params.containsKey("pic_id"))
        assertFalse(params.containsKey("video_id"))
    }

    @Test
    fun imagesAndVideoCannotShareOneStatusRequest() {
        val result = runCatching {
            listOf(
                ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-a", "pic_id"),
                ComposeMediaUploadResult(ComposeMediaKind.Video, "media-a", "media_id"),
            ).toPostRequest(text = "mixed")
        }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message.orEmpty().contains("不能同时发布"))
    }
}
