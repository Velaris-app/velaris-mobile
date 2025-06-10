package com.investrove.ui.feature.insights.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.*
import kotlin.math.roundToInt

@Composable
fun InvestmentTypeComparison(
    investmentTypeDistribution: InvestmentTypeDistribution,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Porównanie typów inwestycji",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val total = investmentTypeDistribution.alternative + investmentTypeDistribution.traditional
            if (total > 0) {
                val alternativePercentage = (investmentTypeDistribution.alternative / total * 100).roundToInt()
                val traditionalPercentage = (investmentTypeDistribution.traditional / total * 100).roundToInt()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Alternatywne")
                        Text(
                            "$alternativePercentage%",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column {
                        Text("Tradycyjne")
                        Text(
                            "$traditionalPercentage%",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            } else {
                Text("Brak danych")
            }
        }
    }
}