package com.investrove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.investrove.ui.navigation.AppNavHost
import com.investrove.ui.navigation.BottomBarNavigation
import com.investrove.ui.navigation.Routes
import com.investrove.ui.theme.InvesTroveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvesTroveApp()
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun InvesTroveApp() {
    val navController = rememberNavController()
    var darkTheme by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Routes.OVERVIEW,
        Routes.ASSETS,
        Routes.INSIGHTS,
        Routes.SETTINGS,
        Routes.ADD_ASSET
    )
    val showBottomBar = currentRoute in bottomBarRoutes

    InvesTroveTheme(darkTheme = darkTheme) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomBarNavigation(navController)
                }
            }
        ) { padding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(padding),
                darkTheme = darkTheme,
                onThemeChange = { darkTheme = it }
            )
        }
    }
}