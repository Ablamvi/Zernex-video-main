package com.zernex.video.player

data class MediaTrackPreferences(
    val preferredAudioLanguage: String = "fr",
    val preferredSubtitleLanguage: String = "fr",
    val subtitleTextSizeSp: Int = 18,
    val subtitleBackgroundEnabled: Boolean = true
)
