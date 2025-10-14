package com.investrove.ui.feature.insights.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.CategoryValue
import com.investrove.ui.common.SectionCard
import kotlin.math.roundToInt

@Composable
fun CategoryDistributionChart(
    categoryDistribution: List<CategoryValue>,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Rozkład kategorii",
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (categoryDistribution.isNotEmpty()) {
                val totalValue = categoryDistribution.sumOf { it.totalValue }
                categoryDistribution.forEach { categoryValue ->
                    val percentage = ((categoryValue.totalValue / totalValue) * 100).roundToInt()
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(categoryValue.category)
                            Text("$percentage%")
                        }
                        LinearProgressIndicator(
                            progress = (categoryValue.totalValue / totalValue).toFloat(),
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