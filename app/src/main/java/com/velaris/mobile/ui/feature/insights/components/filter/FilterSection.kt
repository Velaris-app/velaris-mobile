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
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Date Range",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = selectedDateRange.start.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = {},
                    label = { Text("From") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
                OutlinedTextField(
                    value = selectedDateRange.end.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = {},
                    label = { Text("To") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
            }

            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleSmall
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
                            onCategoriesChange(selectedCategories)
                        },
                        label = { Text(category) }
                    )
                }
            }
        }
    }
}