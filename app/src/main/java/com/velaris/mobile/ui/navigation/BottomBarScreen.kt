package com.velaris.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomBarScreen(val route: String, val label: String, val icon: ImageVector) {
    object Overview : BottomBarScreen(Routes.OVERVIEW, "Overview", Icons.Default.Dashboard)
    object Portfolio : BottomBarScreen(Routes.ASSETS, "Assets", Icons.AutoMirrored.Filled.List)
    object Insights : BottomBarScreen(Routes.INSIGHTS, "Insights", Icons.AutoMirrored.Filled.ShowChart)
    object Settings : BottomBarScreen(Routes.SETTINGS, "Settings", Icons.Default.Settings)
}

@Composable
fun BottomBarNavigation(navController: NavController) {
    val items =
            listOf(
                    BottomBarScreen.Overview,
                    BottomBarScreen.Portfolio,
                    BottomBarScreen.Insights,
                    BottomBarScreen.Settings
            )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                    )
            )
        }
    }
}
