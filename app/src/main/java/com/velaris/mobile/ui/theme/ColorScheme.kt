package com.velaris.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Primary / Accent Colors ---
val VelarisPrimary = Color(0xFF1D2E5A)
val VelarisAccentGold = Color(0xFFD9B56A)
val VelarisAccentCoral = Color(0xFFF47C6B)

// --- Background / Surface Light ---
val VelarisBackgroundLight = Color(0xFFFAF9F7)
val VelarisSurfaceLight = Color(0xFFFFFFFF)

// --- Background / Surface Dark ---
val VelarisBackgroundDark = Color(0xFF121417)
val VelarisSurfaceDark = Color(0xFF1E1F23)

// --- Text Colors ---
val VelarisOnLight = Color(0xFF202124)
val VelarisOnDarkPrimary = Color(0xFFF5F5F5)
val VelarisOnDarkSecondary = Color(0xFFB0B0B0)

// --- Error / Feedback ---
val VelarisError = Color(0xFFD9534F)

val LightColorScheme = lightColorScheme(
    primary = VelarisPrimary,
    onPrimary = Color.White,
    secondary = VelarisAccentGold,
    onSecondary = Color.White,
    tertiary = VelarisAccentCoral,
    onTertiary = Color.White,
    background = VelarisBackgroundLight,
    onBackground = VelarisOnLight,
    surface = VelarisSurfaceLight,
    onSurface = VelarisOnLight,
    error = VelarisError,
    onError = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = VelarisPrimary,
    onPrimary = Color.White,
    secondary = VelarisAccentGold,
    onSecondary = VelarisOnDarkSecondary,
    tertiary = VelarisAccentCoral,
    onTertiary = VelarisOnDarkSecondary,
    background = VelarisBackgroundDark,
    onBackground = VelarisOnDarkPrimary,
    surface = VelarisSurfaceDark,
    onSurface = VelarisOnDarkPrimary,
    error = VelarisError,
    onError = Color.White
)