package com.investrove.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.ui.feature.overview.*
import java.util.Locale

@Composable
fun InvestmentCategoriesCard(categories: List<CategoryStats>, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Investment Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            categories.forEach { category ->
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                            text = "${String.format(Locale.US, "%.1f", category.percentage)}%",
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
                LinearProgressIndicator(
                    progress = { (category.percentage / 100).toFloat() },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}