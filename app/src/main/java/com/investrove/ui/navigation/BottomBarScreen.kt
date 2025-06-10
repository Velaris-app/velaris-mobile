package com.investrove.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Settings

sealed class BottomBarScreen(val route: String, val label: String, val icon: ImageVector) {
    object Portfolio : BottomBarScreen(Routes.Portfolio, "Portfolio", Icons.AutoMirrored.Filled.List)
    object Insights : BottomBarScreen(Routes.Insights, "Insights", Icons.AutoMirrored.Filled.ShowChart)
    object Settings : BottomBarScreen(Routes.Settings, "Settings", Icons.Default.Settings)
}

@Composable
fun BottomBarNavigation(navController: NavController) {
    val items = listOf(
        BottomBarScreen.Portfolio,
        BottomBarScreen.Insights,
        BottomBarScreen.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        if (screen.route == Routes.Portfolio) {
                            navController.popBackStack(Routes.Portfolio, inclusive = false)
                        } else {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    }
}