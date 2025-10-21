package com.velaris.mobile.ui.feature.common.stats.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.Point
import com.velaris.mobile.domain.model.TrendStats
import com.velaris.mobile.ui.common.charts.LabelData
import com.velaris.mobile.ui.common.charts.LineChart
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PerformanceChartCard(
    performanceData: List<TrendStats>,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MM/dd", Locale.US)

    val sortedData = performanceData.sortedBy { it.value }

    val points = performanceData.mapIndexed { index, stat ->
        Point(x = index.toFloat(), y = stat.value.toFloat())
    }

    val labels = sortedData.map { stat ->
        val xLabel = try {
            dateFormat.format(Date.from(stat.date.toInstant()))
        } catch (_: Exception) {
            ""
        }

        val yLabel = when {
            stat.value >= BigDecimal("1000000000") -> "%.1fB".format(stat.value.divide(BigDecimal("1000000000")).toDouble())
            stat.value >= BigDecimal("1000000") -> "%.1fM".format(stat.value.divide(BigDecimal("1000000")).toDouble())
            stat.value >= BigDecimal("1000") -> "%.1fK".format(stat.value.divide(BigDecimal("1000")).toDouble())
            else -> "%.0f".format(stat.value.toDouble())
        }

        LabelData(xLabel = xLabel, yValue = yLabel)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text("Portfolio Value", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))

            if (performanceData.isEmpty()) {
                Text("Not enough data")
            } else {

                LineChart(
                    data = points,
                    labelData = labels,
                    lineColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                )
            }
        }
    }
}