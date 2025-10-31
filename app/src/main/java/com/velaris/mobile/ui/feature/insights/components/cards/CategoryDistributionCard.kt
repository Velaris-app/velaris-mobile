package com.velaris.mobile.ui.feature.insights.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.ui.common.charts.PieChart

@Composable
fun CategoryDistributionCard(
    categoryDistribution: List<CategoryStats>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "Category Distribution",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (categoryDistribution.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No categories",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            } else {
                val values = categoryDistribution.map { it.totalValue.toFloat() }
                val labels = categoryDistribution.map { it.categoryName }

                PieChart(
                    values = values,
                    labels = labels,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }
    }
}