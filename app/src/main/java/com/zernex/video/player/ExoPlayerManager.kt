package com.zernex.video.player

import android.content.Context
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.zernex.video.data.VideoItem

@OptIn(UnstableApi::class)
class ExoPlayerManager(private val context: Context) {

    private val trackSelector = DefaultTrackSelector(context)
    private var loudnessEnhancer: LoudnessEnhancer? = null

    val exoPlayer: ExoPlayer by lazy {
        val renderersFactory = DefaultRenderersFactory(context)
            .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            .setEnableDecoderFallback(true)

        ExoPlayer.Builder(context, renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
    }

    fun initializePlayer(video: VideoItem) {
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(video.videoUrl))
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        try {
            val audioSessionId = exoPlayer.audioSessionId
            if (audioSessionId != C.AUDIO_SESSION_ID_UNSET) {
                loudnessEnhancer = LoudnessEnhancer(audioSessionId).apply {
                    enabled = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setVolumeWithBoost(volumePercent: Int) {
        if (volumePercent <= 100) {
            exoPlayer.volume = volumePercent / 100f
            loudnessEnhancer?.setTargetGain(0)
        } else {
            exoPlayer.volume = 1.0f
            val boostGainMb = ((volumePercent - 100) * 20).coerceIn(0, 2000)
            loudnessEnhancer?.setTargetGain(boostGainMb)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer.playbackParameters = PlaybackParameters(speed)
    }

    fun seekRelative(offsetMs: Long) {
        val newPos = (exoPlayer.currentPosition + offsetMs).coerceIn(0, exoPlayer.duration)
        exoPlayer.seekTo(newPos)
    }

    fun toggleMute() {
        exoPlayer.volume = if (exoPlayer.volume > 0) 0f else 1f
    }

    fun play() = exoPlayer.play()
    fun pause() = exoPlayer.pause()

    fun releasePlayer() {
        loudnessEnhancer?.release()
        loudnessEnhancer = null
        exoPlayer.release()
    }
}
