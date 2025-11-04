package com.velaris.mobile.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.auth.SessionViewModel
import com.velaris.mobile.ui.feature.settings.components.sections.AccountSection
import com.velaris.mobile.ui.feature.settings.components.sections.AppearanceSection
import com.velaris.mobile.ui.feature.settings.components.sections.CurrencySection
import com.velaris.mobile.ui.navigation.BottomBarNavigation

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    navController: NavController,
    viewModel: SessionViewModel
) {
    Scaffold(
        topBar = { CompactTopBar("Settings") },
        bottomBar = { BottomBarNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppearanceSection(isDarkTheme, onThemeChange)
            CurrencySection(viewModel)
            AccountSection(viewModel, navController)
        }
    }
}