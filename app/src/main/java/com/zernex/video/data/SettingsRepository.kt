package com.zernex.video.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository {
    private val _settings = MutableStateFlow(PlaybackSettings())
    val settings: StateFlow<PlaybackSettings> = _settings.asStateFlow()

    fun updateSettings(transform: (PlaybackSettings) -> PlaybackSettings) {
        _settings.value = transform(_settings.value)
    }
}
