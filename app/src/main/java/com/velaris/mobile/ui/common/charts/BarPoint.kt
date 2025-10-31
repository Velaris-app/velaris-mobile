package com.velaris.mobile.ui.common.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

@Composable
fun BarChart(
    points: List<Float>,            // tylko wartości Y
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    showAxes: Boolean = true,
    gridSteps: Int = 4,
    xLabels: List<String>? = null,
    yLabels: List<String>? = null
) {
    if (points.isEmpty()) return

    val axisColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textMeasurer = rememberTextMeasurer()

    val minY = points.minOrNull() ?: 0f
    val maxY = points.maxOrNull() ?: 1f
    val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f

    val minBarHeight = 4f // minimalna wysokość słupka, żeby był widoczny

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1500, 300)
    )
    LaunchedEffect(Unit) { progress = 1f }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var scaleX by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scaleX = (scaleX * zoom).coerceIn(1f, 5f)
                    val maxOffset = 0f
                    val minOffset = -(size.width - 100f) * (scaleX - 1f)
                    offsetX = (offsetX + pan.x).coerceIn(minOffset, maxOffset)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    val leftPadding = 100f
                    val chartWidth = size.width - leftPadding
                    val n = points.size
                    val barSpacing = chartWidth / n
                    selectedIndex = points.indices.minByOrNull { i ->
                        val x = leftPadding + (i + 0.5f) * barSpacing * scaleX + offsetX
                        (tap.x - x).absoluteValue
                    }?.takeIf { i ->
                        val x = leftPadding + (i + 0.5f) * barSpacing * scaleX + offsetX
                        (tap.x - x).absoluteValue < 20f
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val leftPadding = 100f
        val chartHeight = height - 60f
        val chartWidth = width - leftPadding

        if (showAxes) {
            drawAxesAndGrid(
                leftPadding, chartHeight, chartWidth,
                axisColor, gridSteps, minY, maxY,
                xLabels, yLabels,
                textColor, textMeasurer,
                scaleX = scaleX,
                offsetX = offsetX
            )
        }

        val n = points.size
        val barSpacing = chartWidth / n
        val barWidth = barSpacing * 0.6f

        // rysujemy słupki w clipRect
        clipRect(left = leftPadding, top = 0f, right = leftPadding + chartWidth, bottom = chartHeight) {
            points.forEachIndexed { i, yValue ->
                val barHeight = (yValue / maxY * chartHeight * animatedProgress).coerceAtLeast(minBarHeight)
                val barX = leftPadding + (i + 0.5f) * barSpacing * scaleX + offsetX
                drawRect(
                    color = barColor,
                    topLeft = Offset(barX - barWidth / 2f, chartHeight - barHeight),
                    size = Size(barWidth, barHeight)
                )
            }
        }

        // Tooltip dla wybranego słupka
        selectedIndex?.let { i ->
            val normalizedY = (points[i] - minY) / rangeY
            val barHeight = (normalizedY * chartHeight * animatedProgress).coerceAtLeast(minBarHeight)
            val x = leftPadding + (i + 0.5f) * barSpacing * scaleX + offsetX
            val y = chartHeight - barHeight

            drawCircle(color = barColor, radius = 6f, center = Offset(x, y))
            val tooltipText = "x=${xLabels?.getOrNull(i) ?: i}, y=${points[i]}"
            val textWidth = 100f
            val textHeight = 20f
            val textPadding = 8f

            val clampedTooltipX = (x + textPadding).coerceIn(0f, width - textWidth)
            val clampedTooltipY = (y - textHeight - textPadding).coerceIn(0f, height - textHeight)
            drawText(
                textMeasurer,
                tooltipText,
                topLeft = Offset(clampedTooltipX, clampedTooltipY),
                style = TextStyle(color = textColor, fontSize = 12.sp)
            )
        }
    }
}

private fun DrawScope.drawAxesAndGrid(
    leftPadding: Float,
    chartHeight: Float,
    chartWidth: Float,
    axisColor: Color,
    gridSteps: Int,
    minY: Float,
    maxY: Float,
    xLabels: List<String>?,
    yLabels: List<String>?,
    textColor: Color,
    textMeasurer: TextMeasurer,
    scaleX: Float,
    offsetX: Float
) {
    // Oś Y
    drawLine(axisColor, Offset(leftPadding, 0f), Offset(leftPadding, chartHeight), strokeWidth = 2f)
    // Oś X
    drawLine(axisColor, Offset(leftPadding, chartHeight), Offset(leftPadding + chartWidth, chartHeight), strokeWidth = 2f)

    // Linie siatki i etykiety Y
    for (i in 0..gridSteps) {
        val y = chartHeight - chartHeight / gridSteps * i
        drawLine(axisColor.copy(alpha = 0.3f), Offset(leftPadding, y), Offset(leftPadding + chartWidth, y))
        val label = yLabels?.getOrNull(i) ?: String.format("%.1f", minY + (maxY - minY) / gridSteps * i)
        drawText(textMeasurer, label, Offset(leftPadding - 8f * label.length, y - 8f), TextStyle(color = textColor, fontSize = 12.sp))
    }

    // Etykiety X
    xLabels?.forEachIndexed { i, label ->
        val n = xLabels.size
        val barSpacing = chartWidth / n
        val x = leftPadding + (i + 0.5f) * barSpacing * scaleX + offsetX
        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(label),
            style = TextStyle(color = textColor, fontSize = 12.sp)
        )
        drawText(
            textLayoutResult,
            topLeft = Offset(x - textLayoutResult.size.width / 2f, chartHeight + 4f)
        )
    }
}

@Preview
@Composable
fun BarChartPreview() {
    val mockPoints = listOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "Extra")
    MaterialTheme {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            BarChart(mockPoints, xLabels = xLabels)
        }
    }
}