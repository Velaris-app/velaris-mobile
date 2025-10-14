package com.investrove.ui.feature.insights.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.model.*
import com.investrove.ui.common.SectionCard

@Composable
fun TopPerformersCard(
    topPerformers: List<TopPerformer>,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Najlepsze inwestycje",
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (topPerformers.isNotEmpty()) {
                topPerformers.forEach { performer ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = performer.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
//                            Text(
//                                text = performer.category,
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
                        }
//                        Text(
//                            text = "%.2f%%".format(performer.returnPercentage),
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = if (performer.returnPercentage >= 0)
//                                MaterialTheme.colorScheme.primary
//                            else
//                                MaterialTheme.colorScheme.error
//                        )
                    }
                }
            } else {
                Text("Brak danych")
            }
        }
    }
}