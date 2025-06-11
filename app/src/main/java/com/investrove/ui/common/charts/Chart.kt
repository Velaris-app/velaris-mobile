package com.investrove.ui.common.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.investrove.data.model.HistoricalDataPoint

@Composable
fun Chart(
    historicalData: List<HistoricalDataPoint>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (historicalData.size <= 1) return

    val maxValue = historicalData.maxOf { it.value }.toFloat()
    val minValue = historicalData.minOf { it.value }.toFloat()
    val valueRange = (maxValue - minValue).takeIf { it != 0f } ?: 1f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val width = size.width
        val height = size.height
        val path = Path()

        // Grid lines
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

        // Line path
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
            color = lineColor,
            style = Stroke(width = 2f)
        )
    }
}
