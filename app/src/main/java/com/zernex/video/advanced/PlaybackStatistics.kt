package com.zernex.video.advanced

data class PlaybackStatistics(
    val droppedFrames: Int = 0,
    val renderedFrames: Int = 0,
    val currentFps: Double = 60.0,
    val averageBitrateKbps: Int = 18500,
    val totalWatchTimeSeconds: Long = 0
)
