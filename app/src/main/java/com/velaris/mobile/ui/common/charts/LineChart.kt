package com.velaris.mobile.ui.common.charts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

data class LabelData( val xLabel: String, val yValue: String )


@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data: List<Point>,
    labelData: List<LabelData>,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    showEvery: Int = 2,
    maxYLabels: Int = 5
) {
    if (data.isEmpty()) return

    val yAxisData = AxisData.Builder()
        .steps(data.size - 1)
        .axisLabelColor(MaterialTheme.colorScheme.onSurface)
        .axisLineColor(MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))
        .labelAndAxisLinePadding(30.dp)
        .labelData { i ->
            when (i) {
                0 -> labelData.first().yValue
                data.lastIndex -> labelData.last().yValue
                else -> {
                    val middleCount = (maxYLabels - 2).coerceAtLeast(0)
                    if (middleCount == 0) return@labelData ""

                    val step = (data.size - 1).toFloat() / (middleCount + 1)
                    val middleIndexes = (1..middleCount).map { (it * step).toInt() }
                    if (i in middleIndexes) labelData[i].yValue else ""
                }
            }
        }
        .build()

    val xAxisData = AxisData.Builder()
        .steps(data.size - 1)
        .axisStepSize(40.dp)
        .axisLabelColor(MaterialTheme.colorScheme.onSurface)
        .axisLineColor(MaterialTheme.colorScheme.onSurface.copy(alpha = 1f))
        .labelData { i ->
            when {
                i == 0 || i == data.lastIndex -> ""
                i % showEvery == 0 -> labelData[i].xLabel
                else -> ""
            }
        }
        .build()

    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = data,
                lineStyle = LineStyle(color = lineColor),
                intersectionPoint = IntersectionPoint(color = lineColor, radius = 4.dp),
                selectionHighlightPoint = SelectionHighlightPoint(
                    color = MaterialTheme.colorScheme.onSurface,
                    radius = 5.dp
                ),
                shadowUnderLine = ShadowUnderLine(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)
                    ),
                    alpha = 1f
                ),
                selectionHighlightPopUp =
                    SelectionHighlightPopUp()
            )
        )
    )

    val chartData = LineChartData(
        linePlotData = linePlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Transparent,
        isZoomAllowed = false
    )

    LineChart(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp, max = 600.dp),
        lineChartData = chartData
    )
}