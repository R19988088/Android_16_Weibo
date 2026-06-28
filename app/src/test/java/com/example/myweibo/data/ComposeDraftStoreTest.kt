package com.example.myweibo.data

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ComposeDraftStoreTest {
    @Test
    fun recycleBinIncludesFailedDeletedAndExpiredPublishingDrafts() {
        val now = 1_000_000L
        val items = listOf(
            ComposeDraft(id = "editing", updatedAt = now, status = ComposeDraftStatus.Editing, text = "a"),
            ComposeDraft(id = "failed", updatedAt = now, status = ComposeDraftStatus.Failed, text = "b"),
            ComposeDraft(id = "deleted", updatedAt = now, status = ComposeDraftStatus.Deleted, text = "c"),
            ComposeDraft(id = "fresh-publishing", updatedAt = now - 1_000L, status = ComposeDraftStatus.Publishing, text = "d"),
            ComposeDraft(id = "old-publishing", updatedAt = now - ComposeDraftStore.PublishingRecycleAfterMillis - 1, status = ComposeDraftStatus.Publishing, text = "e"),
        )

        val recycled = ComposeDraftStore.recycleBin(items, now).map { it.id }

        assertEquals(listOf("failed", "deleted", "old-publishing"), recycled)
    }

    @Test
    fun storeRoundTripsDraftsWithMediaAndVisibility() {
        val dir = createTempDir(prefix = "compose-drafts-test")
        val store = ComposeDraftStore(dir)
        val draft = ComposeDraft(
            id = "draft-1",
            createdAt = 10,
            updatedAt = 20,
            status = ComposeDraftStatus.Failed,
            type = ComposeDraftType.Status,
            text = "hello",
            media = listOf(
                ComposeDraftMedia(uri = "content://image/1", kind = ComposeMediaKind.Image),
                ComposeDraftMedia(uri = "content://video/1", kind = ComposeMediaKind.Video),
            ),
            visible = ComposeVisibility.Friends,
            errorMessage = "network",
            requestParams = mapOf("content" to "hello"),
        )

        store.save(draft)
        val restored = store.readAll().single()

        assertEquals(draft, restored)
        assertTrue(File(dir, "compose_drafts.json").exists())
    }
}
