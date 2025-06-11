package com.investrove.ui.feature.overview.components.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.investrove.ui.feature.overview.*

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