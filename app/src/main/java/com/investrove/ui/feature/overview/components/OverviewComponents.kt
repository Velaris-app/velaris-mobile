package com.investrove.ui.feature.overview.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.investrove.ui.feature.overview.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TotalValueCard(totalValue: Double, currency: String, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Portfolio Value", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                    text = "${NumberFormat.getCurrencyInstance().format(totalValue)} $currency",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

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
                            text = "${String.format("%.1f", percentage)}%",
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

@Composable
fun PerformanceChartCard(
    performanceData: List<PerformanceDataPoint>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Performance", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (performanceData.isEmpty()) {
                Text(
                    text = "No performance data available",
                    style = MaterialTheme.typography.bodyMedium
                )
                return@Column
            }

            Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                val width = size.width
                val height = size.height
                val maxValue = performanceData.maxOf { it.value }
                val minValue = performanceData.minOf { it.value }
                val valueRange = (maxValue - minValue).takeIf { it != 0.0 } ?: 1.0 // zabezpieczenie przed dzieleniem przez 0

                // Draw grid lines
                val gridLines = 5
                for (i in 0..gridLines) {
                    val y = height * (1 - i.toFloat() / gridLines)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.3f),
                        start = Offset(0f, y),
                        end = Offset(width, y)
                    )
                }

                // Draw line chart
                val points = performanceData.mapIndexed { index, point ->
                    val x = width * index / (performanceData.size - 1).coerceAtLeast(1)
                    val y = height * (1 - (point.value - minValue) / valueRange)
                    Offset(x, y.toFloat())
                }

                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = primaryColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 2f
                    )
                }

                points.forEach { point ->
                    drawCircle(
                        color = primaryColor,
                        radius = 4f,
                        center = point
                    )
                }
            }
        }
    }
}

@Composable
fun InvestmentCategoriesCard(categories: List<CategoryStats>, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Investment Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            categories.forEach { category ->
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                            text = "${String.format("%.1f", category.percentage)}%",
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
                LinearProgressIndicator(
                    progress = { (category.percentage / 100).toFloat() },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RecentActivityCard(activities: List<ActivityItem>, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Recent Activity", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            activities.forEach { activity ->
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                                text = activity.description,
                                style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                                text =
                                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                                .format(Date(activity.date)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                            text =
                                    "${activity.type}: ${NumberFormat.getCurrencyInstance().format(activity.value)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                    if (activity.type == "Purchase") MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.primary
                    )
                }
                if (activity != activities.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
