package com.velaris.mobile.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// --- Primary / Accent Colors ---
val VelarisPrimary = Color(0xFF1D2E5A)    // głęboki granat‑niebieski
val VelarisAccentGold = Color(0xFFD9B56A) // złoto – akcent
val VelarisAccentCoral = Color(0xFFF47C6B) // koralowy – drugi akcent

// --- Background / Surface Light ---
val VelarisBackgroundLight = Color(0xFFFAF9F7)  // bardzo jasny beż/ivory
val VelarisSurfaceLight = Color(0xFFFFFFFF)     // biel

// --- Background / Surface Dark ---
val VelarisBackgroundDark = Color(0xFF121417)   // prawie czarny, lekko granatowy
val VelarisSurfaceDark = Color(0xFF1E1F23)      // ciemna powierzchnia

// --- Text Colors ---
val VelarisOnLight = Color(0xFF202124)  // ciemny tekst na jasnym tle
val VelarisOnDarkPrimary = Color(0xFFF5F5F5)  // prawie biały
val VelarisOnDarkSecondary = Color(0xFFB0B0B0)

// --- Error / Feedback ---
val VelarisError = Color(0xFFD9534F)   // mocny czerwony‑róż

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