package com.investrove.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    var showThemeAnimation by remember { mutableStateOf(false) }
    var animationAsset by remember { mutableStateOf("") }
    var animationJob by remember { mutableStateOf<Job?>(null) }

    val scope = rememberCoroutineScope()

    val composition by rememberLottieComposition(
        if (animationAsset.isNotEmpty()) LottieCompositionSpec.Asset(animationAsset) else LottieCompositionSpec.RawRes(0)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = showThemeAnimation,
        iterations = 1,
        speed = 1.5f,
        restartOnPlay = true
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Settings")

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dark Theme")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { newValue ->
                            animationAsset = if (newValue) "dark_mode.json" else "light_mode.json"
                            showThemeAnimation = true
                            onThemeChange(newValue)

                            animationJob?.cancel()
                            animationJob = scope.launch {
                                delay(2000)
                                showThemeAnimation = false
                                animationJob = null
                            }
                        }
                    )
                }
            }

            if (showThemeAnimation && composition != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(400.dp)
                    )
                }
            }
        }
    }
}