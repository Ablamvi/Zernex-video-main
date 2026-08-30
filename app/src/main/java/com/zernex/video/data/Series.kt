package com.zernex.video.data

data class SeriesShow(
    val id: String,
    val title: String,
    val seasonCount: Int,
    val totalEpisodes: Int,
    val episodes: List<VideoItem>
)

object SeriesParser {
    private val seriesRegex = Regex("(?i)(.*?)[sS](\\d{1,2})[eE](\\d{1,2})")

    fun parseSeriesInfo(filename: String): Pair<String, Pair<Int, Int>>? {
        val match = seriesRegex.find(filename) ?: return null
        val showName = match.groupValues[1].replace(".", " ").trim()
        val season = match.groupValues[2].toIntOrNull() ?: 1
        val episode = match.groupValues[3].toIntOrNull() ?: 1
        return Pair(showName, Pair(season, episode))
    }
}
