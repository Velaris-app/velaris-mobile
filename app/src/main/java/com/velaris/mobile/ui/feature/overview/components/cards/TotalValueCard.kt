package com.velaris.mobile.ui.feature.overview.components.cards

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.velaris.mobile.core.util.formatNumber
import com.velaris.mobile.domain.model.OverviewStats
import com.velaris.mobile.ui.common.SectionCard
import java.math.BigDecimal

@Composable
fun TotalValueCard(
    overviewStats: OverviewStats,
    modifier: Modifier = Modifier
) {
    SectionCard(title = "Total Portfolio Value", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val safeValue = overviewStats.totalValue.max(BigDecimal.ZERO)

            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            val alphaAnim by animateFloatAsState(
                targetValue = if (visible) 1f else 0f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )

            Text(
                text = "${formatNumber(safeValue)} ${overviewStats.currency}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(4.dp))

            val animatedAssets by animateIntAsState(
                targetValue = overviewStats.totalAssets,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )

            val animatedItems by animateIntAsState(
                targetValue = overviewStats.totalItems,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "Assets: $animatedAssets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Items: $animatedItems",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}