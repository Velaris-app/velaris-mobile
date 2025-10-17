package com.velaris.mobile.ui.feature.insights.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velaris.mobile.ui.common.SectionCard
import com.velaris.mobile.ui.feature.insights.components.DateRange
import java.time.format.DateTimeFormatter

@Composable
fun FilterSection(
    selectedDateRange: DateRange,
    availableCategories: List<String>,
    selectedCategories: Set<String>,
    onCategoriesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Filters",
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = selectedDateRange.start.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = {},
                    label = { Text("Od") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
                OutlinedTextField(
                    value = selectedDateRange.end.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = {},
                    label = { Text("Do") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableCategories.forEach { category ->
                    FilterChip(
                        selected = category in selectedCategories,
                        onClick = {
                            val newSelection = selectedCategories.toMutableSet()
                            if (category in selectedCategories) newSelection.remove(category)
                            else newSelection.add(category)
                            onCategoriesChange(newSelection)
                        },
                        label = { Text(category) }
                    )
                }
            }
        }
    }
}