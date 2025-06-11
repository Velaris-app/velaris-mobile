package com.investrove.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun PortfolioDistributionCard(distribution: Map<String, Double>, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Portfolio Distribution", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            val total = distribution.values.sum()
            distribution.forEach { (category, value) ->
                val percentage = (value / total) * 100
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category, style = MaterialTheme.typography.bodyMedium)
                    Text(
                            text = "${String.format(Locale.US, "%.1f", percentage)}%",
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
                LinearProgressIndicator(
                    progress = { (percentage / 100).toFloat() },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}