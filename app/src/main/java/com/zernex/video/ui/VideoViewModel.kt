package com.zernex.video.ui

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.zernex.video.data.FavoritesRepository
import com.zernex.video.data.VideoItem
import com.zernex.video.data.VideoRepository
import com.zernex.video.service.VideoService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class VideoUiState(
    val videos: List<VideoItem> = emptyList(),
    val filteredVideos: List<VideoItem> = emptyList(),
    val favoriteIds: Set<Long> = emptySet(),
    val favoriteVideos: List<VideoItem> = emptyList(),
    val currentVideo: VideoItem? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val hasPermission: Boolean = false,
    val error: String? = null,
    val isPlayerVisible: Boolean = false
)

class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VideoRepository(application)
    private val favoritesRepository = FavoritesRepository(application)
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState.asStateFlow()

    private var mediaController: MediaController? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var positionUpdateJob: Job? = null

    /** Exposé pour lier le PlayerView Compose au même ExoPlayer. */
    val player: Player?
        get() = mediaController

    init {
        connectToService()
        observeFavorites()
    }

    private fun connectToService() {
        val sessionToken = SessionToken(
            getApplication(),
            ComponentName(getApplication(), VideoService::class.java)
        )
        controllerFuture = MediaController.Builder(getApplication(), sessionToken).buildAsync()
        controllerFuture?.addListener({
            mediaController = controllerFuture?.get()
            mediaController?.addListener(playerListener)
            updatePlayerState()
            if (mediaController?.isPlaying == true) {
                startPositionUpdates()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoritesRepository.favoriteIds.collect { ids ->
                _uiState.update { state ->
                    val favs = state.videos.filter { it.id in ids }
                    state.copy(favoriteIds = ids, favoriteVideos = favs)
                }
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _uiState.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) startPositionUpdates() else {
                stopPositionUpdates()
                updatePlayerState()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val uri = mediaItem?.localConfiguration?.uri
            val video = _uiState.value.videos.find { it.uri == uri }
            _uiState.update { it.copy(currentVideo = video) }
            updatePlayerState()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlayerState()
        }

        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY) ||
                events.contains(Player.EVENT_TIMELINE_CHANGED)
            ) {
                updatePlayerState()
            }
        }
    }

    private fun updatePlayerState() {
        mediaController?.let { controller ->
            val duration = controller.duration
            _uiState.update {
                it.copy(
                    isPlaying = controller.isPlaying,
                    currentPosition = controller.currentPosition.coerceAtLeast(0L),
                    duration = if (duration > 0) duration else it.duration
                )
            }
        }
    }

    private fun startPositionUpdates() {
        if (positionUpdateJob?.isActive == true) return
        positionUpdateJob = viewModelScope.launch {
            while (isActive) {
                updatePlayerState()
                delay(200L)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    fun loadVideos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val videos = repository.getAllVideos()
                _uiState.update {
                    val favs = videos.filter { v -> v.id in it.favoriteIds }
                    it.copy(
                        videos = videos,
                        filteredVideos = videos,
                        favoriteVideos = favs,
                        isLoading = false,
                        hasPermission = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur : ${e.message}"
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            val filtered = if (query.isBlank()) {
                state.videos
            } else {
                state.videos.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }
            state.copy(searchQuery = query, filteredVideos = filtered)
        }
    }

    fun playVideo(video: VideoItem, queue: List<VideoItem> = _uiState.value.filteredVideos) {
        mediaController?.let { controller ->
            val mediaItems = queue.map {
                VideoService.createMediaItem(it.uri, it.title)
            }
            val startIndex = queue.indexOfFirst { it.id == video.id }.coerceAtLeast(0)

            controller.setMediaItems(mediaItems, startIndex, 0L)
            controller.prepare()
            controller.play()

            _uiState.update {
                it.copy(
                    currentVideo = video,
                    isPlaying = true,
                    isPlayerVisible = true
                )
            }
            startPositionUpdates()
        }
    }

    fun togglePlayPause() {
        mediaController?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
        _uiState.update { it.copy(currentPosition = position) }
    }

    fun setHasPermission(has: Boolean) {
        _uiState.update { it.copy(hasPermission = has) }
        if (has) loadVideos()
    }

    fun openPlayer() {
        if (_uiState.value.currentVideo != null) {
            _uiState.update { it.copy(isPlayerVisible = true) }
        }
    }

    fun closePlayer() {
        _uiState.update { it.copy(isPlayerVisible = false) }
    }

    fun toggleFavorite(videoId: Long) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(videoId)
        }
    }

    override fun onCleared() {
        stopPositionUpdates()
        mediaController?.removeListener(playerListener)
        controllerFuture?.let { MediaController.releaseFuture(it) }
        super.onCleared()
    }
}
