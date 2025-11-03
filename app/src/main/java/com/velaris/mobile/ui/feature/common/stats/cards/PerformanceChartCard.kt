package com.velaris.mobile.ui.feature.common.stats.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.TrendStats
import com.velaris.mobile.ui.common.charts.ChartPoint
import com.velaris.mobile.ui.common.charts.LineChart
import java.time.format.DateTimeFormatter

@Composable
fun PerformanceChartCard(
    performanceData: List<TrendStats>,
    modifier: Modifier = Modifier
) {
    val dateFormat = DateTimeFormatter.ofPattern("MM/dd")

    if (performanceData.isEmpty()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Portfolio Value", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                Text("Not enough data")
            }
        }
        return
    }

    // punkty do wykresu
    val chartPoints = performanceData.mapIndexed { index, stat ->
        ChartPoint(x = index.toFloat(), y = stat.value.toFloat())
    }

    // labelki X
    val xLabels = performanceData.map { stat ->
        try {
            dateFormat.format(stat.date)
        } catch (_: Exception) { "" }
    }

    val minY = performanceData.minOfOrNull { it.value.toDouble() } ?: 0.0
    val maxY = performanceData.maxOfOrNull { it.value.toDouble() } ?: 1.0

    val ySteps = 5
    val yLabels = (0..ySteps).map { i ->
        val value = minY + (maxY - minY) * i / ySteps
        when {
            value >= 1_000_000_000 -> "%.1fB".format(value / 1_000_000_000)
            value >= 1_000_000 -> "%.1fM".format(value / 1_000_000)
            value >= 1_000 -> "%.1fK".format(value / 1_000)
            else -> "%.0f".format(value)
        }
    }.toMutableList()

    // wymuszenie 0 na dole
    yLabels[0] = "0"

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Portfolio Performance",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))

                LineChart(
                    points = chartPoints,
                    xLabels = xLabels, // nasze labelki X,
                    yLabels = yLabels  // nasze labelki Y
                )
            }
        }
    }
}