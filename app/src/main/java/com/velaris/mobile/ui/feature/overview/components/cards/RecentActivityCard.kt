package com.velaris.mobile.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.ActivityTypeEnum
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun RecentActivityCard(
    activities: List<RecentActivity>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(16.dp))

            if (activities.isEmpty()) {
                Text(
                    text = "No recent activity",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    activities.forEach { activity ->
                        RecentActivityRow(activity)
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentActivityRow(activity: RecentActivity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Asset #${activity.assetId}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (!activity.changedFields.isNullOrEmpty()) {
                Text(
                    text = "Changed fields: ${activity.changedFields.keys.joinToString()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = activity.changeDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            val (text, color) = when (activity.activityType) {
                ActivityTypeEnum.CREATED -> "Added" to MaterialTheme.colorScheme.primary
                ActivityTypeEnum.UPDATED -> "Updated" to MaterialTheme.colorScheme.secondary
                ActivityTypeEnum.DELETED -> "Removed" to MaterialTheme.colorScheme.error
            }

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}