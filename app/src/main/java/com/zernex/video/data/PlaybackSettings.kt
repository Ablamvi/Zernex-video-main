package com.zernex.video.data

data class PlaybackSettings(
    val hwPlusEnabled: Boolean = true,
    val audioBoostPercent: Int = 100, // 0..200%
    val loopABEnabled: Boolean = false,
    val loopA: Long = 0L,
    val loopB: Long = 0L,
    val playbackSpeed: Float = 1.0f,
    val aspectRatioMode: AspectRatioMode = AspectRatioMode.FIT
)

enum class AspectRatioMode {
    FIT,
    FILL_CROP,
    RATIO_16_9,
    RATIO_4_3,
    ORIGINAL
}
