package com.investrove.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.investrove.ui.feature.additem.AddItemScreen
import com.investrove.ui.feature.additem.AddItemViewModel
import com.investrove.ui.feature.insights.InsightsScreen
import com.investrove.ui.feature.overview.OverviewScreen
import com.investrove.ui.feature.portfolio.PortfolioScreen
import com.investrove.ui.feature.portfolio.PortfolioViewModel
import com.investrove.ui.feature.settings.SettingsScreen

object Routes {
    const val Overview = "overview"
    const val Portfolio = "portfolio"
    const val AddItem = "addItem"
    const val Insights = "insights"
    const val Settings = "settings"
}

@Composable
@ExperimentalMaterial3Api
fun AppNavHost(
        navController: NavHostController,
        darkTheme: Boolean,
        onThemeChange: (Boolean) -> Unit,
        modifier: Modifier
) {
    NavHost(
            navController = navController,
            startDestination = Routes.Overview,
            modifier = modifier
    ) {
        composable(Routes.Overview) { OverviewScreen() }

        composable(Routes.Portfolio) {
            val viewModel = hiltViewModel<PortfolioViewModel>()
            PortfolioScreen(navController = navController, viewModel = viewModel)
        }

        composable(Routes.AddItem) {
            val viewModel = hiltViewModel<AddItemViewModel>()
            AddItemScreen(navController = navController, viewModel = viewModel)
        }

        composable(Routes.Insights) { InsightsScreen() }

        composable(Routes.Settings) {
            SettingsScreen(isDarkTheme = darkTheme, onThemeChange = onThemeChange)
        }
    }
}
