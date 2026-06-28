package com.example.myweibo.data

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class ComposeDraftStore(private val dir: File) {
    private val file = File(dir, FileName)

    suspend fun readAllAsync(): List<ComposeDraft> = withContext(Dispatchers.IO) { readAll() }

    fun readAll(): List<ComposeDraft> {
        if (!file.exists()) return emptyList()
        val raw = runCatching { file.readText(Charsets.UTF_8) }.getOrDefault("")
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val arr = JSONArray(raw)
            buildList {
                for (index in 0 until arr.length()) {
                    arr.optJSONObject(index)?.toComposeDraft()?.takeIf { it.id.isNotBlank() }?.let(::add)
                }
            }.sortedByDescending { it.updatedAt }
        }.getOrDefault(emptyList())
    }

    suspend fun saveAsync(draft: ComposeDraft) = withContext(Dispatchers.IO) { save(draft) }

    fun save(draft: ComposeDraft) {
        val next = readAll()
            .filterNot { it.id == draft.id }
            .plus(draft.copy(updatedAt = draft.updatedAt.takeIf { it > 0 } ?: System.currentTimeMillis()))
            .sortedByDescending { it.updatedAt }
        write(next)
    }

    suspend fun replaceAllAsync(drafts: List<ComposeDraft>) = withContext(Dispatchers.IO) { write(drafts) }

    fun write(drafts: List<ComposeDraft>) {
        if (!dir.exists()) dir.mkdirs()
        val arr = JSONArray()
        drafts.sortedByDescending { it.updatedAt }.forEach { arr.put(it.toJson()) }
        file.writeText(arr.toString(), Charsets.UTF_8)
    }

    companion object {
        const val PublishingRecycleAfterMillis = 30L * 60L * 1000L
        private const val FileName = "compose_drafts.json"

        fun recycleBin(items: List<ComposeDraft>, now: Long = System.currentTimeMillis()): List<ComposeDraft> =
            items
                .filter { draft ->
                    draft.status == ComposeDraftStatus.Failed ||
                        draft.status == ComposeDraftStatus.Deleted ||
                        (draft.status == ComposeDraftStatus.Publishing && now - draft.updatedAt > PublishingRecycleAfterMillis)
                }
                .sortedByDescending { it.updatedAt }
    }
}
