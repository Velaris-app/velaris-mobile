package com.velaris.mobile.ui.feature.assets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.assets.components.AssetsGrid
import com.velaris.mobile.ui.feature.assets.components.states.EmptyState
import com.velaris.mobile.ui.feature.assets.components.states.ErrorBanner
import com.velaris.mobile.ui.navigation.BottomBarNavigation
import com.velaris.mobile.ui.navigation.Routes

@Composable
fun AssetsScreen(
    viewModel: AssetsViewModel,
    navController: NavController
) {
    val assets by viewModel.assets.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD_ASSET) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        },
        topBar = { CompactTopBar("Assets") },
        bottomBar = { BottomBarNavigation(navController) }
    ) { innerPadding  ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            state = pullRefreshState,
            isRefreshing = loading,
            onRefresh = { viewModel.loadAssets() },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = loading,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullRefreshState
                )
            }
        ) {
            when {
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorBanner(error)
                    }
                }

                assets.isEmpty() && !loading -> EmptyState()

                else -> AssetsGrid(
                    assets = assets,
                    onDelete = { id -> id?.let { viewModel.deleteAsset(it) } },
                )
            }
        }

    }
}