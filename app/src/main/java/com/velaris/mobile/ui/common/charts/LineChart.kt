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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

data class ChartPoint(val x: Float, val y: Float)

@Composable
fun LineChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 4f,
    showAxes: Boolean = true,
    gridSteps: Int = 4,
    xLabels: List<String>? = null,
    yLabels: List<String>? = null
) {
    if (points.isEmpty()) return

    val tooltipBackgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    val tooltipTextStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
    val lineColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    val textMeasurer = rememberTextMeasurer()

    val minY = points.minOf { it.y }
    val maxY = points.maxOf { it.y }
    val rangeY = (maxY - minY).takeIf { it != 0f } ?: 1f
    val normalizedPoints = points.map { ChartPoint(it.x, (it.y - minY) / rangeY) }

    val minX = normalizedPoints.minOf { it.x }
    val maxX = normalizedPoints.maxOf { it.x }
    val rangeX = (maxX - minX).takeIf { it > 0f } ?: 1f

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1500, 300)
    )
    LaunchedEffect(Unit) { progress = 1f }

    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
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
                    selectedPointIndex = normalizedPoints.mapIndexed { i, p ->
                        val x = leftPadding + ((p.x - minX) / rangeX) * chartWidth * scaleX + offsetX
                        i to (tap.x - x).absoluteValue
                    }.minByOrNull { it.second }?.takeIf { it.second < 20f }?.first
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val leftPadding = 100f
        val chartHeight = height - 60f
        val chartWidth = width - leftPadding
        val gradient = Brush.verticalGradient(
            colors = listOf(lineColor.copy(alpha = 0.4f), lineColor.copy(alpha = 0.05f))
        )

        if (showAxes) {
            drawAxesAndGrid(
                leftPadding, chartHeight, chartWidth,
                axisColor, gridSteps, minY.toFloat(), maxY.toFloat(),
                xLabels, yLabels,
                textColor, textMeasurer,
                scaleX = scaleX,
                offsetX = offsetX
            )
        }

        val linePath = Path()
        val fillPath = Path()
        normalizedPoints.forEachIndexed { i, p ->
            val x = leftPadding + ((p.x - minX) / rangeX) * chartWidth * scaleX + offsetX
            val y = chartHeight - (p.y * chartHeight)
            if (i == 0) {
                linePath.moveTo(x, y)
                fillPath.moveTo(x, chartHeight)
                fillPath.lineTo(x, y)
            } else {
                val prevX = leftPadding + ((normalizedPoints[i - 1].x - minX) / rangeX) * chartWidth * scaleX + offsetX
                val prevY = chartHeight - (normalizedPoints[i - 1].y * chartHeight)
                val currentX = prevX + (x - prevX) * animatedProgress
                val currentY = prevY + (y - prevY) * animatedProgress
                linePath.lineTo(currentX, currentY)
                fillPath.lineTo(currentX, currentY)
            }
        }

        val clipRect = Rect(leftPadding, 0f, width, chartHeight)
        clipPath(Path().apply { addRect(clipRect) }) {
            drawPath(
                linePath,
                color = lineColor,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
            fillPath.lineTo(width, chartHeight)
            fillPath.lineTo(leftPadding, chartHeight)
            fillPath.close()
            drawPath(fillPath, brush = gradient)
        }

        selectedPointIndex?.let { index ->
            val p = normalizedPoints[index]
            val x = leftPadding + ((p.x - minX) / rangeX) * chartWidth * scaleX + offsetX
            val y = chartHeight - (p.y * chartHeight)

            // Punkt
            drawCircle(color = lineColor, radius = 6f, center = Offset(x, y))

            val tooltipText = "x=${xLabels?.get(index) ?: points[index].x}, y=${points[index].y}"
            val textLayout = textMeasurer.measure(
                text = tooltipText,
                style = TextStyle(color = textColor, fontSize = 12.sp)
            )

            val textWidth = textLayout.size.width.toFloat() + 12f
            val textHeight = textLayout.size.height.toFloat() + 8f
            val textPadding = 8f

            // ✅ Przypnij tooltip do ekranu, ale licz z uwzględnieniem zoomu i offsetu
            val clampedTooltipX = (x + textPadding)
                .coerceIn(leftPadding, width - textWidth - textPadding)
            val clampedTooltipY = (y - textHeight - textPadding)
                .coerceIn(0f, height - textHeight - textPadding)

            // Tło (lekki bubble)
            drawRoundRect(
                color = tooltipBackgroundColor,
                topLeft = Offset(clampedTooltipX, clampedTooltipY),
                size = androidx.compose.ui.geometry.Size(textWidth, textHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Tekst
            drawText(
                textMeasurer,
                tooltipText,
                topLeft = Offset(clampedTooltipX + 6f, clampedTooltipY + 4f),
                style = tooltipTextStyle
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
    xLabels: List<String>? = null,
    yLabels: List<String>? = null,
    textColor: Color,
    textMeasurer: TextMeasurer,
    scaleX: Float = 1f,
    offsetX: Float = 0f
) {
    val textStyle = TextStyle(color = textColor, fontSize = 10.sp, textAlign = TextAlign.Center)
    val xAxisY = chartHeight
    val minLabelSpacingPx = 8f

    // 🔧 Odstępy od osi
    val yLabelPadding = 6f    // odległość Y-label od osi Y

    // Y-axis
    drawLine(axisColor, Offset(leftPadding, 0f), Offset(leftPadding, chartHeight), 1.5f)
    // X-axis
    drawLine(axisColor, Offset(leftPadding, xAxisY), Offset(leftPadding + chartWidth, xAxisY), 1.5f)

    // === Y grid + labels ===
    for (i in 0..gridSteps) {
        val ratio = i / gridSteps.toFloat()
        val y = chartHeight - ratio * chartHeight
        val value = yLabels?.getOrNull(i) ?: String.format("%.1f", minY + (maxY - minY) * ratio)

        drawLine(
            axisColor.copy(alpha = 0.3f),
            Offset(leftPadding, y),
            Offset(leftPadding + chartWidth, y),
            1f
        )

        val textLayout = textMeasurer.measure(value, style = textStyle)
        val textHeight = textLayout.size.height.toFloat()

        // 🔧 odsunięcie labelki od osi Y o `yLabelPadding`
        val textX = leftPadding - textLayout.size.width - yLabelPadding
        val textY = (y - textHeight / 2f).coerceIn(0f, chartHeight - textHeight)

        drawText(textMeasurer, value, Offset(textX, textY), textStyle)
    }

    // === X ticks + labels ===
    val totalLabels = xLabels?.size ?: 1
    if (totalLabels <= 1) return

    var lastLabelRight = Float.NEGATIVE_INFINITY

    for (i in 0 until totalLabels) {
        val label = xLabels?.getOrNull(i) ?: i.toString()

        val relativeX = (i / (totalLabels - 1f)) * chartWidth
        val x = leftPadding + relativeX * scaleX + offsetX

        if (x < leftPadding - 60f || x > leftPadding + chartWidth + 60f) continue

        val textWidth = textMeasurer.measure(label, style = textStyle).size.width.toFloat()
        val halfWidth = textWidth / 2f

        if (x - halfWidth < lastLabelRight + minLabelSpacingPx) continue

        // tick mark
        drawLine(axisColor, Offset(x, xAxisY - 4f), Offset(x, xAxisY + 4f), 1.2f)

        // 🔧 dodaj odstęp od osi X
        val textX = (x - halfWidth)
            .coerceAtLeast(leftPadding)
            .coerceAtMost(leftPadding + chartWidth - textWidth)
        val labelY = xAxisY + textStyle.fontSize.toPx()

        drawText(
            textMeasurer,
            label,
            Offset(textX, labelY),
            textStyle
        )

        lastLabelRight = textX + textWidth
    }
}

@Preview
@Composable
fun LineChartPreview() {
    val mockPoints = listOf(
        ChartPoint(0f, 3f),
        ChartPoint(1f, 5f),
        ChartPoint(2f, 4f),
        ChartPoint(3f, 6f),
        ChartPoint(4f, 9f),
        ChartPoint(5f, 8f),
        ChartPoint(6f, 10f),
        ChartPoint(7f, 7f)
    )
    MaterialTheme {
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            LineChart(mockPoints)
        }
    }
}