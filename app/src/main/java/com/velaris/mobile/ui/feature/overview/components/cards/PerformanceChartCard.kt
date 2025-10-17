package com.velaris.mobile.ui.feature.overview.components.cards

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.TrendStats
import kotlin.math.roundToInt
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PerformanceChartCard(
    performanceData: List<TrendStats>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val increaseColor = Color(0xFF4CAF50)
    val decreaseColor = Color(0xFFF44336)
    val neutralBandColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
    val gridLineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Performance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                val leftPadding = 70f
                val rightPadding = 40f
                val topPadding = 30f
                val bottomPadding = 70f

                val chartWidth = size.width - leftPadding - rightPadding
                val chartHeight = size.height - topPadding - bottomPadding

                val paintY = Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 28f
                    isAntiAlias = true
                }

                if (performanceData.size > 1) {
                    val values = performanceData.map { it.value.toDouble() }
                    val maxValue = (values.maxOrNull() ?: 1.0) * 1.1
                    val minValue = (values.minOrNull() ?: 0.0) * 0.9
                    val valueRange = (maxValue - minValue).takeIf { it != 0.0 } ?: 1.0

                    val bandCount = 6
                    val bandHeight = chartHeight / bandCount
                    repeat(bandCount) { i ->
                        if (i % 2 == 0) {
                            drawRect(
                                color = neutralBandColor,
                                topLeft = Offset(leftPadding, topPadding + i * bandHeight),
                                size = Size(chartWidth, bandHeight)
                            )
                        }
                    }

                    val gridLines = 5
                    for (i in 0..gridLines) {
                        val y = topPadding + chartHeight * (1 - i.toFloat() / gridLines)
                        drawLine(
                            color = gridLineColor,
                            start = Offset(leftPadding, y),
                            end = Offset(leftPadding + chartWidth, y),
                            strokeWidth = 1f
                        )
                        val label = minValue + i * valueRange / gridLines
                        drawContext.canvas.nativeCanvas.drawText(
                            "%.0f".format(label),
                            10f,
                            y + 10f,
                            paintY
                        )
                    }

                    val stepX = chartWidth / (performanceData.size - 1)
                    val points = performanceData.mapIndexed { index, point ->
                        val x = leftPadding + index * stepX
                        val y = topPadding + chartHeight * (1 - (point.value.toDouble() - minValue) / valueRange)
                        Offset(x, y.toFloat())
                    }

                    if (points.size > 1) {
                        val path = Path().apply {
                            moveTo(points.first().x, topPadding + chartHeight)
                            points.forEach { lineTo(it.x, it.y) }
                            lineTo(points.last().x, topPadding + chartHeight)
                            close()
                        }
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    primaryColor.copy(alpha = 0.25f),
                                    primaryColor.copy(alpha = 0.05f)
                                )
                            )
                        )
                    }

                    for (i in 0 until points.size - 1) {
                        val color = if (values[i + 1] > values[i]) increaseColor else decreaseColor
                        drawLine(
                            color = color,
                            start = points[i],
                            end = points[i + 1],
                            strokeWidth = 4f
                        )
                    }

                    points.forEach {
                        drawCircle(color = primaryColor.copy(alpha = 0.3f), radius = 7f, center = it)
                        drawCircle(color = primaryColor, radius = 3.5f, center = it)
                    }

                    val labelCount = minOf(5, performanceData.size)
                    val labelStep = if (labelCount > 1) (performanceData.size - 1).toFloat() / (labelCount - 1) else 0f
                    val dateFormatter = SimpleDateFormat("MM/dd", Locale.getDefault())
                    val paintX = Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 26f
                        isAntiAlias = true
                    }

                    for (i in 0 until labelCount) {
                        val index = if (labelCount > 1) (i * labelStep).roundToInt() else 0
                        val safeIndex = index.coerceIn(0, performanceData.lastIndex)
                        val label = performanceData[safeIndex].date?.let {
                            try { dateFormatter.format(it) } catch (_: Exception) { it.toString().take(10) }
                        } ?: "P${safeIndex + 1}"

                        val x = leftPadding + safeIndex * stepX
                        drawContext.canvas.nativeCanvas.drawText(
                            label,
                            x - 35f,
                            size.height - 15f,
                            paintX
                        )
                    }
                } else {
                    val text = "Not enough data to display chart"
                    drawContext.canvas.nativeCanvas.apply {
                        val paint = Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 28f
                            isAntiAlias = true
                            textAlign = Paint.Align.CENTER
                        }
                        drawText(
                            text,
                            size.width / 2,
                            size.height / 2 + 10f,
                            paint
                        )
                    }
                }

                drawLine(
                    color = gridLineColor,
                    start = Offset(leftPadding, topPadding),
                    end = Offset(leftPadding, topPadding + chartHeight),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = gridLineColor,
                    start = Offset(leftPadding, topPadding + chartHeight),
                    end = Offset(leftPadding + chartWidth, topPadding + chartHeight),
                    strokeWidth = 1.5f
                )
            }
        }
    }
}