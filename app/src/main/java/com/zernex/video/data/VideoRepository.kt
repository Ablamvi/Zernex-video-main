package com.zernex.video.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoRepository {

    suspend fun scanDeviceVideos(): List<VideoItem> = withContext(Dispatchers.IO) {
        return@withContext getAllMockVideos()
    }

    fun getAllMockVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "v1",
                title = "CYBERPUNK 2077 // NIGHT CITY 4K HDR",
                filename = "night_city_showcase_4k.mkv",
                path = "/storage/emulated/0/Movies/4K/night_city_showcase_4k.mkv",
                folder = "4K Showcase",
                duration = 184,
                sizeBytes = 2_400_000_000L,
                dateAdded = "2026-08-28",
                resolution = "4K UHD (3840x2160)",
                videoCodec = "HEVC / H.265 (Main 10@L5.1@Main)",
                audioCodec = "Dolby Atmos / TrueHD 7.1",
                bitrateKbps = 48500,
                fps = 60,
                container = "MKV",
                thumbnailUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                isFavorite = true,
                tags = listOf("4K", "HDR", "60 FPS")
            ),
            VideoItem(
                id = "v2",
                title = "ATTACK ON TITAN // S04E28 FINAL SEASON",
                filename = "aot_s04e28_the_dawn_of_humanity_1080p.mkv",
                path = "/storage/emulated/0/Anime/Shingeki no Kyojin/aot_s04e28.mkv",
                folder = "Anime",
                duration = 1420,
                sizeBytes = 1_450_000_000L,
                dateAdded = "2026-08-29",
                resolution = "1080p FHD (1920x1080)",
                videoCodec = "AVC / H.264 (High@L4.1)",
                audioCodec = "FLAC 2.0 (24-bit Lossless)",
                bitrateKbps = 12400,
                fps = 24,
                container = "MKV",
                thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                isFavorite = true,
                tags = listOf("Anime", "Série", "FHD")
            )
        )
    }
}
