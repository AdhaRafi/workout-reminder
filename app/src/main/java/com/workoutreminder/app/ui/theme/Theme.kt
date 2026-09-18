package com.workoutreminder.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CoralOrange,
    onPrimary = Color.White,
    primaryContainer = CoralOrangeDark,
    onPrimaryContainer = Color.White,
    secondary = ActiveGreen,
    onSecondary = DarkBackgroundRaw,
    secondaryContainer = ActiveGreenMuted,
    onSecondaryContainer = ActiveGreen,
    background = DarkBackgroundRaw,
    onBackground = TextWhiteRaw,
    surface = DarkSurfaceRaw,
    onSurface = TextWhiteRaw,
    surfaceVariant = DarkSurfaceElevatedRaw,
    onSurfaceVariant = TextGrayLightRaw,
    outline = DarkBorderRaw,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CoralOrange,
    onPrimary = Color.White,
    primaryContainer = CoralOrangeLight,
    onPrimaryContainer = Color.White,
    secondary = ActiveGreen,
    onSecondary = Color.White,
    secondaryContainer = ActiveGreenMuted,
    onSecondaryContainer = Color(0xFF0F5132),
    background = LightBackgroundRaw,
    onBackground = LightTextWhiteRaw,
    surface = LightSurfaceRaw,
    onSurface = LightTextWhiteRaw,
    surfaceVariant = LightSurfaceElevatedRaw,
    onSurfaceVariant = LightTextGrayLightRaw,
    outline = LightBorderRaw,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun WorkoutReminderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appThemeColors = if (darkTheme) DarkThemeColors else LightThemeColors
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val bgArgb = if (darkTheme) DarkBackgroundRaw.toArgb() else LightBackgroundRaw.toArgb()
                window.statusBarColor = bgArgb
                window.navigationBarColor = bgArgb
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalAppThemeColors provides appThemeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
