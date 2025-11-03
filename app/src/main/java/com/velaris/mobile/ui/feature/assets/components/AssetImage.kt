package com.velaris.mobile.ui.feature.assets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.velaris.mobile.R
import com.velaris.mobile.domain.model.AssetItem

@Composable
internal fun AssetImage(asset: AssetItem) {
    val context = LocalContext.current
    val imageUrl = remember(asset.images) { asset.images.firstOrNull() }

    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        result.throwable.printStackTrace()
                    }
                )
                .build(),
            contentDescription = asset.name,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_broken_image),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
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