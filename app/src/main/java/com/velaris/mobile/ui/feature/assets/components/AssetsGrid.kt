package com.velaris.mobile.ui.feature.assets.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velaris.mobile.ui.feature.assets.components.cards.AssetCard
import com.velaris.api.client.model.Asset

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AssetsGrid(
    assets: List<Asset>,
    onDelete: (Long?) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, bottom = 80.dp)
    ) {
        items(assets, key = { it.id ?: it.hashCode() }) { asset ->
            AssetCard(
                asset = asset,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                onDelete = { onDelete(asset.id) }
            )
        }
    }
}