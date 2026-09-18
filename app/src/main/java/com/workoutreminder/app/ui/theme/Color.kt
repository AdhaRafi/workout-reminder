package com.workoutreminder.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Brand Accents
val CoralOrange = Color(0xFFFF5A3C)
val CoralOrangeLight = Color(0xFFFF7E67)
val CoralOrangeDark = Color(0xFFD43B1E)
val CoralOrangeMuted = Color(0x20FF5A3C)

val ActiveGreen = Color(0xFF3DDC84)
val ActiveGreenMuted = Color(0x203DDC84)

// Alert / Error
val ErrorRed = Color(0xFFFF453A)

// --- Raw Theme Palette Definitions ---
// Dark Palette (Raw)
val DarkBackgroundRaw = Color(0xFF121212)
val DarkSurfaceRaw = Color(0xFF1E1E1E)
val DarkSurfaceElevatedRaw = Color(0xFF252525)
val DarkSurfaceCardRaw = Color(0xFF1C1C1E)
val DarkBorderRaw = Color(0xFF333336)
val TextWhiteRaw = Color(0xFFFFFFFF)
val TextGrayLightRaw = Color(0xFFE2E2E6)
val TextGrayMutedRaw = Color(0xFF8E8E93)
val TextGrayDarkRaw = Color(0xFF636366)
val ChipBackgroundDarkRaw = Color(0xFF2C2C2E)

// Light Palette (Raw)
val LightBackgroundRaw = Color(0xFFF6F7F9)
val LightSurfaceRaw = Color(0xFFFFFFFF)
val LightSurfaceElevatedRaw = Color(0xFFF1F3F5)
val LightSurfaceCardRaw = Color(0xFFFFFFFF)
val LightBorderRaw = Color(0xFFE2E8F0)
val LightTextWhiteRaw = Color(0xFF0F172A)     // Primary text in light mode
val LightTextGrayLightRaw = Color(0xFF334155) // Secondary text in light mode
val LightTextGrayMutedRaw = Color(0xFF64748B) // Muted text in light mode
val LightTextGrayDarkRaw = Color(0xFF94A3B8)
val ChipBackgroundLightRaw = Color(0xFFE2E8F0)

// Chip / Badge Colors
val ChipSelectedBackground = Color(0xFFFF5A3C)
val ChipSelectedText = Color(0xFFFFFFFF)

@Immutable
data class AppThemeColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceCard: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textDark: Color,
    val chipBackground: Color,
    val isDark: Boolean
)

val DarkThemeColors = AppThemeColors(
    background = DarkBackgroundRaw,
    surface = DarkSurfaceRaw,
    surfaceElevated = DarkSurfaceElevatedRaw,
    surfaceCard = DarkSurfaceCardRaw,
    border = DarkBorderRaw,
    textPrimary = TextWhiteRaw,
    textSecondary = TextGrayLightRaw,
    textMuted = TextGrayMutedRaw,
    textDark = TextGrayDarkRaw,
    chipBackground = ChipBackgroundDarkRaw,
    isDark = true
)

val LightThemeColors = AppThemeColors(
    background = LightBackgroundRaw,
    surface = LightSurfaceRaw,
    surfaceElevated = LightSurfaceElevatedRaw,
    surfaceCard = LightSurfaceCardRaw,
    border = LightBorderRaw,
    textPrimary = LightTextWhiteRaw,
    textSecondary = LightTextGrayLightRaw,
    textMuted = LightTextGrayMutedRaw,
    textDark = LightTextGrayDarkRaw,
    chipBackground = ChipBackgroundLightRaw,
    isDark = false
)

val LocalAppThemeColors = staticCompositionLocalOf { DarkThemeColors }

// Dynamic Accessors (Seamlessly switches all screens between Dark and Light mode)
val DarkBackground: Color
    @Composable
    get() = LocalAppThemeColors.current.background

val DarkSurface: Color
    @Composable
    get() = LocalAppThemeColors.current.surface

val DarkSurfaceElevated: Color
    @Composable
    get() = LocalAppThemeColors.current.surfaceElevated

val DarkSurfaceCard: Color
    @Composable
    get() = LocalAppThemeColors.current.surfaceCard

val DarkBorder: Color
    @Composable
    get() = LocalAppThemeColors.current.border

val TextWhite: Color
    @Composable
    get() = LocalAppThemeColors.current.textPrimary

val TextGrayLight: Color
    @Composable
    get() = LocalAppThemeColors.current.textSecondary

val TextGrayMuted: Color
    @Composable
    get() = LocalAppThemeColors.current.textMuted

val TextGrayDark: Color
    @Composable
    get() = LocalAppThemeColors.current.textDark

val ChipBackground: Color
    @Composable
    get() = LocalAppThemeColors.current.chipBackground
