package com.investrove.ui.feature.insights.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.CategoryStats
import kotlin.math.roundToInt

@Composable
fun CategoryDistributionChart(
    categoryDistribution: Map<String, CategoryStats>,
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
                text = "Rozkład kategorii",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (categoryDistribution.isNotEmpty()) {
                val totalValue = categoryDistribution.values.sumOf { it.totalValue }
                categoryDistribution.forEach { (category, stats) ->
                    val percentage = ((stats.totalValue / totalValue) * 100).roundToInt()
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(category)
                            Text("$percentage%")
                        }
                        LinearProgressIndicator(
                            progress = { (stats.totalValue / totalValue).toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                }
            } else {
                Text("Brak danych")
            }
        }
    }
}