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
            ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-a", "pic_id", fid = "pic-a"),
            ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-b", "pic_id", fid = "pic-b"),
        ).toPostRequest(text = "with images").toWebParams()

        assertTrue(params["media"].orEmpty().contains("\"fid\":\"pic-a\""))
        assertTrue(params["media"].orEmpty().contains("\"fid\":\"pic-b\""))
        assertFalse(params.containsKey("pic_id"))
        assertFalse(params.containsKey("video_id"))
    }

    @Test
    fun uploadedVideoKeepsPublishParamName() {
        val params = listOf(
            ComposeMediaUploadResult(ComposeMediaKind.Video, "media-a", "media_id", fid = "fid-a"),
        ).toPostRequest(text = "with video").toWebParams()

        assertTrue(params["media"].orEmpty().contains("\"media_id\":\"media-a\""))
        assertTrue(params["media"].orEmpty().contains("\"fid\":\"fid-a\""))
        assertFalse(params.containsKey("pic_id"))
        assertFalse(params.containsKey("media_id"))
    }

    @Test
    fun imagesAndVideoCanShareOneMixedMediaRequest() {
        val params = listOf(
            ComposeMediaUploadResult(ComposeMediaKind.Image, "pic-a", "pic_id", fid = "pic-a"),
            ComposeMediaUploadResult(ComposeMediaKind.Video, "media-a", "media_id", fid = "fid-a"),
        ).toPostRequest(text = "mixed").toWebParams()

        val media = params["media"].orEmpty()
        assertTrue(media.contains("\"type\":\"pic\""))
        assertTrue(media.contains("\"type\":\"video\""))
        assertTrue(media.contains("\"fid\":\"pic-a\""))
        assertTrue(media.contains("\"media_id\":\"media-a\""))
    }
}
