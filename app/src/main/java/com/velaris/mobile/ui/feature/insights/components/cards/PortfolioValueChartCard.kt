package com.velaris.mobile.ui.feature.insights.components.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.TrendStats

@Composable
fun PortfolioValueChartCard(
    historicalData: List<TrendStats>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Portfolio Value", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            if (historicalData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data")
                }
            } else {
                val maxVal = historicalData.maxOf { it.value.toDouble() }
                val minVal = historicalData.minOf { it.value.toDouble() }

                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)) {
                    val widthStep = size.width / (historicalData.size - 1)
                    val heightScale = if (maxVal - minVal == 0.0) 1f else size.height / (maxVal - minVal).toFloat()

                    historicalData.forEachIndexed { index, point ->
                        if (index > 0) {
                            val prev = historicalData[index - 1]
                            drawLine(
                                color = primaryColor,
                                start = Offset(
                                    x = widthStep * (index - 1),
                                    y = size.height - (prev.value.toDouble() - minVal).toFloat() * heightScale
                                ),
                                end = Offset(
                                    x = widthStep * index,
                                    y = size.height - (point.value.toDouble() - minVal).toFloat() * heightScale
                                ),
                                strokeWidth = 3f
                            )
                        }
                    }
                }
            }
        }
    }
}
