package com.zernex.video.data

import android.net.Uri

data class VideoItem(
    val id: Long,
    val title: String,
    val duration: Long,
    val size: Long,
    val uri: Uri,
    val path: String = "",
    val dateAdded: Long = 0L,
    val width: Int = 0,
    val height: Int = 0
) {
    val durationFormatted: String
        get() {
            val totalSeconds = duration / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return if (hours > 0) {
                "%d:%02d:%02d".format(hours, minutes, seconds)
            } else {
                "%d:%02d".format(minutes, seconds)
            }
        }

    val sizeFormatted: String
        get() {
            val mb = size / (1024.0 * 1024.0)
            return if (mb >= 1024) {
                "%.1f Go".format(mb / 1024)
            } else {
                "%.0f Mo".format(mb)
            }
        }

    val resolutionLabel: String
        get() = when {
            height >= 2160 || width >= 3840 -> "4K"
            height >= 1080 || width >= 1920 -> "1080p"
            height >= 720 || width >= 1280 -> "720p"
            height > 0 -> "${height}p"
            else -> ""
        }
}
