package com.velaris.mobile.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.velaris.mobile.ui.feature.assets.AddAssetScreen
import com.velaris.mobile.ui.feature.assets.AssetsScreen
import com.velaris.mobile.ui.feature.assets.AssetsViewModel
import com.velaris.mobile.ui.feature.auth.LoginScreen
import com.velaris.mobile.ui.feature.auth.RegisterScreen
import com.velaris.mobile.ui.feature.auth.SessionViewModel
import com.velaris.mobile.ui.feature.insights.InsightsScreen
import com.velaris.mobile.ui.feature.overview.OverviewScreen
import com.velaris.mobile.ui.feature.settings.SettingsScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val OVERVIEW = "overview"
    const val ADD_ASSET = "addAsset"
    const val ASSETS = "assets"
    const val INSIGHTS = "insights"
    const val SETTINGS = "settings"
}


@Composable
@ExperimentalMaterial3Api
fun AppNavHost(
    navController: NavHostController,
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    modifier: Modifier
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.checkTokenValidity()
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.OVERVIEW else Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.OVERVIEW) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.OVERVIEW) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.OVERVIEW) { OverviewScreen() }

        composable(Routes.ASSETS) {
            val viewModel = hiltViewModel<AssetsViewModel>()
            AssetsScreen(navController = navController, viewModel = viewModel)
        }

        composable(Routes.ADD_ASSET) {
            val viewModel = hiltViewModel<AssetsViewModel>()
            AddAssetScreen(navController = navController, viewModel = viewModel)
        }

        composable(Routes.INSIGHTS) { InsightsScreen() }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                isDarkTheme = darkTheme,
                onThemeChange = onThemeChange,
                navController = navController
            )
        }
    }
}
