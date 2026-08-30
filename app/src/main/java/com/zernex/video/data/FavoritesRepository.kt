package com.zernex.video.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository {
    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf("v1", "v2"))
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    fun toggleFavorite(videoId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(videoId)) {
            current.remove(videoId)
        } else {
            current.add(videoId)
        }
        _favoriteIds.value = current
    }

    fun isFavorite(videoId: String): Boolean = _favoriteIds.value.contains(videoId)
}
