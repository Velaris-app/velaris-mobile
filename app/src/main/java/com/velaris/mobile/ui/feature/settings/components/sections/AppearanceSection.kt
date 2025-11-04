package com.velaris.mobile.ui.feature.settings.components.sections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.velaris.mobile.ui.common.SectionCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppearanceSection(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SectionCard(title = "Appearance") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dark Theme", style = MaterialTheme.typography.bodyLarge)

            Switch(
                checked = isDarkTheme,
                onCheckedChange = { newValue ->
                    if (!isAnimating) {
                        isAnimating = true
                        onThemeChange(newValue)
                        scope.launch {
                            delay(300)
                            isAnimating = false
                        }
                    }
                },
                thumbContent = {
                    AnimatedContent(
                        targetState = isDarkTheme,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                        label = "theme_icon"
                    ) { isDark ->
                        Icon(
                            imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}
