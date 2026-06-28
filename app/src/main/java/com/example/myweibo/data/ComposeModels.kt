package com.example.myweibo.data

import org.json.JSONArray
import org.json.JSONObject

enum class ComposeMediaKind {
    Image,
    Video,
}

enum class ComposeDraftStatus {
    Editing,
    Publishing,
    Failed,
    Deleted,
    Sent,
}

enum class ComposeDraftType {
    Status,
    Repost,
    Edit,
}

enum class ComposeVisibility(val label: String, val webValue: String) {
    Public("公开", "0"),
    Friends("好友圈", "6"),
    Private("仅自己可见", "1"),
}

data class ComposeDraftMedia(
    val uri: String,
    val kind: ComposeMediaKind,
)

data class ComposeDraft(
    val id: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
    val status: ComposeDraftStatus = ComposeDraftStatus.Editing,
    val type: ComposeDraftType = ComposeDraftType.Status,
    val text: String = "",
    val media: List<ComposeDraftMedia> = emptyList(),
    val visible: ComposeVisibility = ComposeVisibility.Public,
    val errorMessage: String? = null,
    val requestParams: Map<String, String> = emptyMap(),
) {
    val isBlank: Boolean
        get() = text.isBlank() && media.isEmpty()
}

data class ComposePostRequest(
    val text: String,
    val picIds: List<String> = emptyList(),
    val videoId: String? = null,
    val videoParamName: String = "video_id",
    val visibility: ComposeVisibility = ComposeVisibility.Public,
) {
    fun requireValid() {
        require(text.trim().isNotBlank() || picIds.isNotEmpty() || !videoId.isNullOrBlank()) {
            "发帖内容不能为空"
        }
    }

    fun toWebParams(): LinkedHashMap<String, String> {
        requireValid()
        return linkedMapOf(
            "content" to text.trim(),
            "visible" to visibility.webValue,
            "sync_mblog" to "1",
            "isReEdit" to "false",
            "topic_id" to "",
        ).apply {
            if (picIds.isNotEmpty()) put("pic_id", picIds.joinToString(","))
            videoId?.takeIf { it.isNotBlank() }?.let { put(videoParamName, it) }
        }
    }
}

data class ComposeMediaUploadResult(
    val kind: ComposeMediaKind,
    val mediaId: String,
    val publishParamName: String,
    val thumbnailId: String? = null,
) {
    init {
        require(mediaId.isNotBlank()) { "媒体上传结果不能为空" }
        require(publishParamName.isNotBlank()) { "媒体发布参数不能为空" }
    }
}

fun Iterable<ComposeMediaUploadResult>.toPostRequest(
    text: String,
    visibility: ComposeVisibility = ComposeVisibility.Public,
): ComposePostRequest {
    val images = filter { it.kind == ComposeMediaKind.Image }.map { it.mediaId }
    val videoResult = firstOrNull { it.kind == ComposeMediaKind.Video }
    require(images.isEmpty() || videoResult == null) { "图片和视频不能同时发布" }
    return ComposePostRequest(
        text = text,
        picIds = images,
        videoId = videoResult?.mediaId,
        videoParamName = videoResult?.publishParamName ?: "video_id",
        visibility = visibility,
    )
}

internal fun ComposeDraft.toJson(): JSONObject =
    JSONObject()
        .put("id", id)
        .put("createdAt", createdAt)
        .put("updatedAt", updatedAt)
        .put("status", status.name)
        .put("type", type.name)
        .put("text", text)
        .put("media", JSONArray().also { arr ->
            media.forEach { item ->
                arr.put(JSONObject().put("uri", item.uri).put("kind", item.kind.name))
            }
        })
        .put("visible", visible.name)
        .put("errorMessage", errorMessage)
        .put("requestParams", JSONObject().also { params ->
            requestParams.forEach { (key, value) -> params.put(key, value) }
        })

internal fun JSONObject.toComposeDraft(): ComposeDraft =
    ComposeDraft(
        id = optString("id"),
        createdAt = optLong("createdAt"),
        updatedAt = optLong("updatedAt"),
        status = enumValueOfOrDefault(optString("status"), ComposeDraftStatus.Editing),
        type = enumValueOfOrDefault(optString("type"), ComposeDraftType.Status),
        text = optString("text"),
        media = buildList {
            val arr = optJSONArray("media") ?: return@buildList
            for (index in 0 until arr.length()) {
                val item = arr.optJSONObject(index) ?: continue
                val uri = item.optString("uri").takeIf { it.isNotBlank() } ?: continue
                add(
                    ComposeDraftMedia(
                        uri = uri,
                        kind = enumValueOfOrDefault(item.optString("kind"), ComposeMediaKind.Image),
                    ),
                )
            }
        },
        visible = enumValueOfOrDefault(optString("visible"), ComposeVisibility.Public),
        errorMessage = if (isNull("errorMessage")) null else optString("errorMessage"),
        requestParams = buildMap {
            val params = optJSONObject("requestParams") ?: return@buildMap
            val keys = params.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                put(key, params.optString(key))
            }
        },
    )

private inline fun <reified T : Enum<T>> enumValueOfOrDefault(value: String?, default: T): T =
    enumValues<T>().firstOrNull { it.name == value } ?: default
