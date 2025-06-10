package com.investrove.ui.feature.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.investrove.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) { viewModel.refreshPortfolio() }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("InvesTrove - Portfolio") },
                        actions = {
                            IconButton(onClick = { viewModel.refreshPortfolio() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                            }
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate(Routes.AddItem) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Investment")
                }
            }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
            state.error != null -> {
                Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                ) {
                    Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${state.error}")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refreshPortfolio() }) { Text("Retry") }
                    }
                }
            }
            state.items.isEmpty() -> {
                Box(
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                ) {
                    Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                    ) {
                        Text("No investments yet")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.navigate(Routes.AddItem) }) {
                            Text("Add Your First Investment")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                        modifier = Modifier.padding(padding),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items) { item ->
                        PortfolioItemCard(item = item, onClick = { /* TODO: Navigate to details */})
                    }
                }
            }
        }
    }
}
