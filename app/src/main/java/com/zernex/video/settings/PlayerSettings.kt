package com.zernex.video.settings

data class PlayerSettings(
    val hardwareAcceleration: Boolean = true,
    val audioBoost: Boolean = true,
    val maxAudioVolume: Int = 200,
    val defaultSubtitleLanguage: String = "fr",
    val gestureBrightness: Boolean = true,
    val gestureVolume: Boolean = true,
    val doubleTapSeekSeconds: Int = 10,
    val amoledPureBlack: Boolean = true
)
