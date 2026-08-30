package com.zernex.video.search

import com.zernex.video.data.VideoItem

class SearchEngine {
    fun search(query: String, items: List<VideoItem>): List<VideoItem> {
        if (query.isBlank()) return items
        val q = query.lowercase().trim()
        return items.filter {
            it.title.lowercase().contains(q) ||
            it.filename.lowercase().contains(q) ||
            it.tags.any { tag -> tag.lowercase().contains(q) } ||
            it.videoCodec.lowercase().contains(q)
        }
    }
}
