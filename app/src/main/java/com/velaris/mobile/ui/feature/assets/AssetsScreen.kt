package com.velaris.mobile.ui.feature.assets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.assets.components.AssetsGrid
import com.velaris.mobile.ui.feature.assets.components.states.EmptyState
import com.velaris.mobile.ui.feature.assets.components.states.ErrorBanner
import com.velaris.mobile.ui.feature.assets.components.states.LoadingState
import com.velaris.mobile.ui.navigation.Routes

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AssetsScreen(
    viewModel: AssetsViewModel,
    navController: NavController
) {
    val assets by viewModel.assets.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadAssets() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.ADD_ASSET) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        },
        topBar = { CompactTopBar("Assets") }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                loading -> LoadingState()
                error != null -> ErrorBanner(error)
                assets.isEmpty() -> EmptyState()
                else -> AssetsGrid(
                    assets = assets,
                    onDelete = { id -> id?.let { viewModel.deleteAsset(it) } }
                )
            }
        }
    }
}
