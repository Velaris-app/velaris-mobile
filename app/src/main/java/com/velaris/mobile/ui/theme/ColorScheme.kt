package com.velaris.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Warm Blue / Gold palette ---
val WarmBluePrimary = Color(0xFF2A3A5E)      // Deep warm navy
val WarmGold = Color(0xFFD4A373)             // Warm gold accent
val WarmCoral = Color(0xFFF28482)            // Soft coral
val WarmBackgroundLight = Color(0xFFF9F7F4)  // Light beige/ivory
val WarmBackgroundDark = Color(0xFF121212)   // True dark background
val WarmSurfaceLight = Color(0xFFFFFFFF)     // Clean white
val WarmSurfaceDark = Color(0xFF1E1E1E)      // Dark surface, neutral
val WarmError = Color(0xFFD9534F)            // Error red

// --- Text colors ---
val OnLight = Color(0xFF1E1E1E)  // Dark text on light
val OnDark = Color(0xFFE6E6E6)   // Light text on dark

// --- Light Color Scheme ---
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

// --- Dark Color Scheme ---
val DarkColorScheme = darkColorScheme(
    primary = WarmBluePrimary,
    onPrimary = Color.White,
    secondary = WarmGold,
    onSecondary = Color.Black,          // lepszy kontrast na ciemnym tle
    tertiary = WarmCoral,
    onTertiary = Color.Black,           // kontrast dla ciemnego tła
    background = WarmBackgroundDark,
    onBackground = OnDark,
    surface = WarmSurfaceDark,
    onSurface = OnDark,
    surfaceVariant = Color(0xFF2A2A2C), // lekko jaśniejsza od surface, dla karty/paneli
    onSurfaceVariant = Color(0xFFD1D1D1),
    error = WarmError,
    onError = Color.White
)