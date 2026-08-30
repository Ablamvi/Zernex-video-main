package com.zernex.video.data

data class AudioTrack(
    val id: String,
    val name: String,
    val language: String,
    val channels: String,
    val codec: String
)

data class SubtitleTrack(
    val id: String,
    val name: String,
    val language: String,
    val format: String,
    val isEmbedded: Boolean
)

data class VideoItem(
    val id: String,
    val title: String,
    val filename: String,
    val path: String,
    val folder: String,
    val duration: Int,
    val currentTime: Int = 0,
    val sizeBytes: Long,
    val dateAdded: String,
    val resolution: String,
    val width: Int = 1920,
    val height: Int = 1080,
    val videoCodec: String,
    val audioCodec: String,
    val bitrateKbps: Int,
    val fps: Int,
    val container: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val isFavorite: Boolean = false,
    val tags: List<String> = emptyList(),
    val audioTracks: List<AudioTrack> = emptyList(),
    val subtitleTracks: List<SubtitleTrack> = emptyList()
) {
    val formattedDuration: String
        get() {
            val minutes = duration / 60
            val seconds = duration % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    val formattedSize: String
        get() {
            val mb = sizeBytes / (1024 * 1024).toDouble()
            return if (mb >= 1024) {
                String.format("%.1f GB", mb / 1024)
            } else {
                String.format("%.0f MB", mb)
            }
        }
}
