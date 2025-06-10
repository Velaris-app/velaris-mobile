package com.investrove.ui.feature.insights.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.investrove.data.model.HistoricalDataPoint

@Composable
fun PortfolioValueChart(
    historicalData: List<HistoricalDataPoint>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Wartość portfela",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (historicalData.size > 1) {
                val maxValue = historicalData.maxOf { it.value }.toFloat()
                val minValue = historicalData.minOf { it.value }.toFloat()
                val valueRange = (maxValue - minValue).takeIf { it != 0f } ?: 1f

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    val width = size.width
                    val height = size.height
                    val path = Path()

                    // Draw grid lines
                    val gridLines = 5
                    val gridSpacing = height / gridLines
                    repeat(gridLines + 1) { i ->
                        val y = i * gridSpacing
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.3f),
                            start = Offset(0f, y),
                            end = Offset(width, y)
                        )
                    }

                    // Draw value path
                    historicalData.forEachIndexed { index, point ->
                        val x = (index.toFloat() / (historicalData.size - 1)) * width
                        val y = height - ((point.value.toFloat() - minValue) / valueRange * height)
                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = primaryColor,
                        style = Stroke(width = 2f)
                    )
                }

                // Value labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "%.2f zł".format(minValue),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "%.2f zł".format(maxValue),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Brak danych")
                }
            }
        }
    }
}