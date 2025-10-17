package com.velaris.mobile.ui.feature.assets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.velaris.api.client.model.Asset

@Composable
internal fun AssetImage(asset: Asset) {
    val imageUrl = remember(asset.images) { asset.images?.firstOrNull() }
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = asset.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text("No image", style = MaterialTheme.typography.bodySmall)
        }
    }
}