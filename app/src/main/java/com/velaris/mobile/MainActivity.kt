package com.velaris.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.velaris.mobile.ui.feature.auth.SessionState
import com.velaris.mobile.ui.feature.auth.SessionViewModel
import com.velaris.mobile.ui.navigation.AppNavHost
import com.velaris.mobile.ui.navigation.Routes
import com.velaris.mobile.ui.theme.VelarisTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            sessionViewModel.isLoading.value
        }

        sessionViewModel.checkTokenValidity()

        setContent {
            VelarisApp(sessionViewModel)
        }
    }
}

@Composable
fun VelarisApp(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()
    var darkTheme by remember { mutableStateOf(false) }
    val sessionState by sessionViewModel.sessionState.collectAsState()

    if (sessionState == SessionState.Loading) return

    val startDestination = when (sessionState) {
        SessionState.LoggedIn -> Routes.OVERVIEW
        else -> Routes.LOGIN
    }

    VelarisTheme(darkTheme = darkTheme) {
        AppNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize().systemBarsPadding() ,
            darkTheme = darkTheme,
            onThemeChange = { darkTheme = it },
            startDestination = startDestination
        )
    }
}