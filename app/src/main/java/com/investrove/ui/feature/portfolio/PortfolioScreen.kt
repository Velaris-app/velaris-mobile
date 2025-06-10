package com.investrove.ui.feature.portfolio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = { TopAppBar(title = { Text("InvesTrove - Portfolio") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addItem") }) {
                Text("+")
            }
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Error: ${state.error}")
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(state.items) { item ->
                        PortfolioItemCard(item = item, onClick = { /* */ })
                    }
                }
            }
        }
    }
}
