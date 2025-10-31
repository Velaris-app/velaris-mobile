package com.velaris.mobile.ui.feature.insights.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.TopMoversStats
import com.velaris.mobile.util.formatNumber

@Composable
fun TopPerformersCard(
    topPerformers: List<TopMoversStats>,
    modifier: Modifier = Modifier
) {
    val maxValue = topPerformers.maxOfOrNull { it.totalValue.toDouble() }?.coerceAtLeast(1.0) ?: 1.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Top Performers",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(Modifier.height(16.dp))

            if (topPerformers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No top performers yet",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    topPerformers.forEachIndexed { index, item ->
                        TopPerformerRow(item, maxValue)
                        if (index != topPerformers.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopPerformerRow(item: TopMoversStats, maxValue: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            if (!item.category.isNullOrEmpty()) {
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(0.5f)
                .height(8.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((item.totalValue.toDouble() / maxValue).toFloat())
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = "${formatNumber(item.totalValue)} PLN",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)