package com.investrove.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val BluePrimary = Color(0xFF264653)
val GoldSecondary = Color(0xFFE9C46A)
val CoralTertiary = Color(0xFFF4A261)
val DeepRedError = Color(0xFFE76F51)

val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = GoldSecondary,
    onSecondary = Color.Black,
    tertiary = CoralTertiary,
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFAAAAAA),
    error = DeepRedError,
    onError = Color.White
)

val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = GoldSecondary,
    onSecondary = Color.Black,
    tertiary = CoralTertiary,
    onTertiary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF49454F),
    error = DeepRedError,
    onError = Color.White
)