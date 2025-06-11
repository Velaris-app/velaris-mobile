package com.investrove.ui.feature.settings

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.investrove.ui.common.SectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    var isAnimating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                                    kotlinx.coroutines.delay(300)
                                    isAnimating = false
                                }
                            }
                        },
                        thumbContent = {
                            AnimatedContent(
                                targetState = isDarkTheme,
                                transitionSpec = {
                                    fadeIn(animationSpec = tween(300)).togetherWith(
                                        fadeOut(animationSpec = tween(300))
                                    )
                                },
                                label = "theme_icon"
                            ) { isDark ->
                                Icon(
                                    imageVector =
                                        if (isDark) Icons.Default.DarkMode
                                        else Icons.Default.LightMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(SwitchDefaults.IconSize)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}