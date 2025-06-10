package com.investrove.ui.feature.insights.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.*

@Composable
fun PerformanceMetricsCard(
    portfolioStats: PortfolioStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Metryki wydajności",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "Całkowity zwrot",
                    value = "%.2f%%".format(portfolioStats.totalReturn)
                )
                MetricItem(
                    label = "Miesięczny zwrot",
                    value = "%.2f%%".format(portfolioStats.monthlyReturn)
                )
            }
        }
    }
}