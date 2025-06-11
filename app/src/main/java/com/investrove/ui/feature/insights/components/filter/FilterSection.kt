package com.investrove.ui.feature.insights.components.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.investrove.data.repository.DateRange
import com.investrove.ui.common.SectionCard
import java.time.format.DateTimeFormatter

@Composable
fun FilterSection(
    selectedDateRange: DateRange,
    selectedCategories: Set<String>,
    onDateRangeChange: (DateRange) -> Unit,
    onCategoriesChange: (Set<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "Mieszkanie", "Złoto", "Waluta", "Akcje", "Fundusz ETF",
        "Kryptowaluty", "Figurka", "LEGO", "Karta Kolekcjonerska"
    )

    SectionCard(
        title = "Filtry",
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Date range selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = selectedDateRange.start.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = { onDateRangeChange },
                    label = { Text("Od") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = selectedDateRange.end.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    onValueChange = { },
                    label = { Text("Do") },
                    modifier = Modifier.weight(1f),
                    enabled = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category filters
            Text(
                text = "Kategorie",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    FilterChip(
                        selected = category in selectedCategories,
                        onClick = {
                            val newSelection = selectedCategories.toMutableSet()
                            if (category in selectedCategories) {
                                newSelection.remove(category)
                            } else {
                                newSelection.add(category)
                            }
                            onCategoriesChange(newSelection)
                        },
                        label = { Text(category) }
                    )
                }
            }
        }
    }
}