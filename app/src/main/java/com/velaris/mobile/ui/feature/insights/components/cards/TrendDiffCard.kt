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
import com.velaris.mobile.domain.model.TrendDiffStats
import com.velaris.mobile.util.formatNumber
import java.math.BigDecimal

@Composable
fun TrendDiffCard(
    trendDiff: List<TrendDiffStats>,
    modifier: Modifier = Modifier
) {
    val maxDelta = (trendDiff.maxOfOrNull { it.deltaValue.toDouble() } ?: 1.0).coerceAtLeast(1.0)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Trend Diff",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(Modifier.height(16.dp))

            if (trendDiff.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No trend data available",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    trendDiff.forEachIndexed { index, item ->
                        TrendDiffRow(item, maxDelta)
                        if (index != trendDiff.lastIndex) {
                            HorizontalDivider(
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
private fun TrendDiffRow(item: TrendDiffStats, maxDelta: Double) {
    val isPositive = item.deltaValue >= BigDecimal.ZERO
    val color = if (isPositive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date
        Text(
            text = item.date.toLocalDate().toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.width(100.dp)
        )

        // Delta bar
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth((item.deltaValue.toDouble() / maxDelta).toFloat())
                    .background(color, shape = RoundedCornerShape(4.dp))
            )
        }

        Spacer(Modifier.width(12.dp))

        // Delta value & percent
        Text(
            text = formatNumber(item.deltaValue) + " PLN",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "${item.deltaPercent}%",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (item.deltaPercent >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        )
    }
}