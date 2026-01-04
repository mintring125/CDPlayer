package com.example.cdplayer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CDPrimary,
    onPrimary = CDOnPrimary,
    primaryContainer = CDPrimaryVariant,
    onPrimaryContainer = CDOnPrimary,
    secondary = CDSecondary,
    onSecondary = CDOnSecondary,
    background = CDBackground,
    onBackground = CDOnBackground,
    surface = CDSurface,
    onSurface = CDOnSurface,
    surfaceVariant = CDSurfaceVariant,
    onSurfaceVariant = Color(0xFFC7B8D6) // Light purple-grey for text on variants
)

private val LightColorScheme = lightColorScheme(
    primary = CDPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = CDPrimaryVariant,
    onPrimaryContainer = Color.White,
    secondary = CDSecondary,
    onSecondary = Color.White,
    background = CDBackgroundLight,
    onBackground = CDOnBackgroundLight,
    surface = CDSurfaceLight,
    onSurface = CDOnSurfaceLight,
    surfaceVariant = Color(0xFFF0EBF5),   // Very light purple-grey
    onSurfaceVariant = Color(0xFF5E506B)
)

@Composable
fun CDPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
