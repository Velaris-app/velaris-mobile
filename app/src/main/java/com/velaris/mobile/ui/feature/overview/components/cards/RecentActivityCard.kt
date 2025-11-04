package com.velaris.mobile.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.ActivityTypeEnum
import com.velaris.mobile.ui.common.SectionCard
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun RecentActivityCard(
    activities: List<RecentActivity>,
    modifier: Modifier = Modifier,
    maxVisibleItems: Int = 5,
    rowModifier: Modifier = Modifier
) {
    SectionCard(title = "Recent Activity", modifier = modifier) {
        if (activities.isEmpty()) {
            Text(
                text = "No recent activity",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val rowHeight = 80.dp
            val maxHeight = rowHeight * maxVisibleItems

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                activities.forEach { activity ->
                    RecentActivityRow(activity, modifier = rowModifier)
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}

@Composable
fun RecentActivityRow(
    activity: RecentActivity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                text = activity.changeDate.format(
                    DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.getDefault())
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        val (text, color) = when (activity.activityType) {
            ActivityTypeEnum.CREATED -> "Added" to MaterialTheme.colorScheme.primary
            ActivityTypeEnum.UPDATED -> "Updated" to MaterialTheme.colorScheme.secondary
            ActivityTypeEnum.DELETED -> "Removed" to MaterialTheme.colorScheme.error
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.align(Alignment.Top)
        )
    }
}