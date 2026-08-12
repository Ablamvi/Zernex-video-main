package com.zernex.video.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6B35),
    onPrimary = Color.White,
    secondary = Color(0xFF00D9FF),
    onSecondary = Color.Black,
    background = Color(0xFF0F0F1A),
    onBackground = Color(0xFFEAEAEA),
    surface = Color(0xFF1A1A2E),
    onSurface = Color(0xFFEAEAEA),
    surfaceVariant = Color(0xFF252540),
    onSurfaceVariant = Color(0xFFB0B0C0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE55A2B),
    onPrimary = Color.White,
    secondary = Color(0xFF0088AA),
    onSecondary = Color.White,
    background = Color(0xFFF5F5F7),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E)
)

@Composable
fun ZernexVideoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
