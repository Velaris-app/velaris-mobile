package com.investrove.ui.feature.assets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.investrove.ui.navigation.Routes
import com.velaris.api.client.model.Asset

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
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Your Assets") },
            )
        }
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

@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
private fun EmptyState() {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "No assets yet.\nAdd your first investment!",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorBanner(error: String?) {
    AnimatedVisibility(visible = !error.isNullOrEmpty()) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AssetsGrid(
    assets: List<Asset>,
    onDelete: (String?) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(assets, key = { it.id ?: it.hashCode() }) { asset ->
            AssetCard(
                asset = asset,
                modifier = Modifier.fillMaxWidth(),
                onDelete = { onDelete(asset.id) }
            )
        }
    }
}

@Composable
fun AssetCard(
    asset: Asset,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    var showConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(210.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                AssetImage(asset)
                Spacer(Modifier.height(8.dp))

                Text(
                    text = asset.name ?: "Unnamed",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = asset.category ?: "No category",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Qty: ${asset.quantity ?: 0}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        asset.purchasePrice?.toPlainString() ?: "-",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            IconButton(
                onClick = { showConfirm = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Asset",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onDelete()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Asset") },
            text = { Text("Are you sure you want to remove this asset?") }
        )
    }
}

@Composable
private fun AssetImage(asset: Asset) {
    val imageUrl = remember(asset.images) { asset.images?.firstOrNull() }
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = asset.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text("No image", style = MaterialTheme.typography.bodySmall)
        }
    }
}
