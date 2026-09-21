package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GitHubBlue,
    onPrimary = Color.White,
    primaryContainer = GitHubDarkCard,
    onPrimaryContainer = GitHubCyan,
    secondary = GitHubPurple,
    onSecondary = Color.White,
    tertiary = GitHubGreenBright,
    background = GitHubDarkBg,
    onBackground = GitHubDarkTextPrimary,
    surface = GitHubDarkSurface,
    onSurface = GitHubDarkTextPrimary,
    surfaceVariant = GitHubDarkCard,
    onSurfaceVariant = GitHubDarkTextSecondary,
    outline = GitHubDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = GitHubBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDF4FF),
    onPrimaryContainer = Color(0xFF0969DA),
    secondary = GitHubPurple,
    onSecondary = Color.White,
    tertiary = GitHubGreen,
    background = GitHubLightBg,
    onBackground = GitHubLightTextPrimary,
    surface = GitHubLightSurface,
    onSurface = GitHubLightTextPrimary,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = GitHubLightTextSecondary,
    outline = GitHubLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our sleek GitHub theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
