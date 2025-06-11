package com.investrove.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.ui.feature.overview.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecentActivityCard(activities: List<ActivityItem>, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Recent Activity", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            activities.forEach { activity ->
                Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                                text = activity.description,
                                style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                                text =
                                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                                .format(Date(activity.date)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                            text =
                                    "${activity.type}: ${NumberFormat.getCurrencyInstance().format(activity.value)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                    if (activity.type == "Purchase") MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.primary
                    )
                }
                if (activity != activities.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}