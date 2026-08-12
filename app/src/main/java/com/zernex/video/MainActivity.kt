package com.zernex.video

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.zernex.video.ui.VideoViewModel
import com.zernex.video.ui.screens.MainScreen
import com.zernex.video.ui.theme.ZernexVideoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: VideoViewModel by viewModels()

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        viewModel.setHasPermission(granted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkAndRequestPermissions()

        setContent {
            ZernexVideoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val uiState by viewModel.uiState.collectAsState()
                    MainScreen(
                        uiState = uiState,
                        player = viewModel.player,
                        onPlayVideo = { video, queue -> viewModel.playVideo(video, queue) },
                        onTogglePlayPause = viewModel::togglePlayPause,
                        onNext = viewModel::skipToNext,
                        onPrevious = viewModel::skipToPrevious,
                        onSeek = viewModel::seekTo,
                        onSearch = viewModel::onSearchQueryChange,
                        onRequestPermission = { checkAndRequestPermissions() },
                        onOpenPlayer = viewModel::openPlayer,
                        onClosePlayer = viewModel::closePlayer,
                        onToggleFavorite = viewModel::toggleFavorite
                    )
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_VIDEO)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isEmpty()) {
            viewModel.setHasPermission(true)
        } else {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }
}
