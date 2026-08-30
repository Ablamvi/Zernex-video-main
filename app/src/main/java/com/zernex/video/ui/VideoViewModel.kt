package com.zernex.video.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zernex.video.data.VideoItem
import com.zernex.video.data.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _videos = MutableStateFlow<List<VideoItem>>(emptyList())
    val videos: StateFlow<List<VideoItem>> = _videos.asStateFlow()

    private val _activeVideo = MutableStateFlow<VideoItem?>(null)
    val activeVideo: StateFlow<VideoItem?> = _activeVideo.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTag = MutableStateFlow("Tous")
    val selectedTag: StateFlow<String> = _selectedTag.asStateFlow()

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _videos.value = repository.scanDeviceVideos()
        }
    }

    fun playVideo(video: VideoItem) {
        _activeVideo.value = video
    }

    fun closePlayer() {
        _activeVideo.value = null
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterVideos()
    }

    fun setSelectedTag(tag: String) {
        _selectedTag.value = tag
        filterVideos()
    }

    fun toggleFavorite(videoId: String) {
        _videos.value = _videos.value.map {
            if (it.id == videoId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    private fun filterVideos() {
        val query = _searchQuery.value.trim().lowercase()
        val tag = _selectedTag.value

        _videos.value = repository.getAllMockVideos().filter { video ->
            val matchesQuery = query.isEmpty() ||
                    video.title.lowercase().contains(query) ||
                    video.videoCodec.lowercase().contains(query)
            val matchesTag = tag == "Tous" || video.tags.contains(tag)
            matchesQuery && matchesTag
        }
    }
}
