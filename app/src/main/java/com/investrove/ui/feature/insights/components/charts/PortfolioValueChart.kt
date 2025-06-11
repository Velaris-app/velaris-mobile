package com.investrove.ui.feature.insights.components.charts

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.HistoricalDataPoint
import com.investrove.ui.common.SectionCard
import com.investrove.ui.common.charts.Chart

@Composable
fun PortfolioValueChart(
    historicalData: List<HistoricalDataPoint>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    SectionCard(
        title = "Wartość portfela",
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (historicalData.size > 1) {
                Chart(
                    historicalData = historicalData,
                    lineColor = primaryColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val minValue = historicalData.minOf { it.value }
                    val maxValue = historicalData.maxOf { it.value }

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