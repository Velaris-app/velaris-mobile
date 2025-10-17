package com.velaris.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Warm Blue / Gold palette ---
val WarmBluePrimary = Color(0xFF2A3A5E)     // Deep warm navy
val WarmGold = Color(0xFFD4A373)            // Warm gold accent
val WarmCoral = Color(0xFFF28482)           // Soft coral
val WarmBackgroundLight = Color(0xFFFAF8F6) // Light beige background
val WarmBackgroundDark = Color(0xFF1C1C1E)  // Dark gray-blue background
val WarmSurfaceLight = Color(0xFFFFFFFF)
val WarmSurfaceDark = Color(0xFF2C2C2E)
val WarmError = Color(0xFFD9534F)

// --- Text colors ---
val OnLight = Color(0xFF1E1E1E)
val OnDark = Color(0xFFF9FAFB)

// --- Color Schemes ---
val LightColorScheme = lightColorScheme(
    primary = WarmBluePrimary,
    onPrimary = Color.White,
    secondary = WarmGold,
    onSecondary = Color.White,
    tertiary = WarmCoral,
    onTertiary = Color.White,
    background = WarmBackgroundLight,
    onBackground = OnLight,
    surface = WarmSurfaceLight,
    onSurface = OnLight,
    surfaceVariant = Color(0xFFF1EDE9),
    onSurfaceVariant = Color(0xFF4A4A4A),
    error = WarmError,
    onError = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = WarmBluePrimary,
    onPrimary = Color.White,
    secondary = WarmGold,
    onSecondary = OnDark,
    tertiary = WarmCoral,
    onTertiary = OnDark,
    background = WarmBackgroundDark,
    onBackground = OnDark,
    surface = WarmSurfaceDark,
    onSurface = OnDark,
    surfaceVariant = Color(0xFF3A3A3C),
    onSurfaceVariant = Color(0xFFC8C8C8),
    error = WarmError,
    onError = Color.White
)