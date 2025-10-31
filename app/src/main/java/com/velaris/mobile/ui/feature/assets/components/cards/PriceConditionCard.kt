package com.velaris.mobile.ui.feature.assets.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

data class PriceConditionState(
    val price: String,
    val currency: String,
    val condition: String,
    val year: String,
    val onPriceChange: (String) -> Unit,
    val onCurrencyChange: (String) -> Unit,
    val onConditionChange: (String) -> Unit,
    val onYearChange: (String) -> Unit
)

@Composable
internal fun PriceConditionCard(
    state: PriceConditionState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Price & Condition",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            RowFields(
                fields = listOf(
                    FieldData(state.price, state.onPriceChange, "Price", KeyboardType.Number),
                    FieldData(state.currency, state.onCurrencyChange, "Currency")
                )
            )

            RowFields(
                fields = listOf(
                    FieldData(state.condition, state.onConditionChange, "Condition"),
                    FieldData(state.year, state.onYearChange, "Year", KeyboardType.Number)
                )
            )
        }
    }
}

@Composable
private fun RowFields(fields: List<FieldData>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        fields.forEach { field ->
            OutlinedTextField(
                value = field.value,
                onValueChange = field.onValueChange,
                label = { Text(field.label) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType ?: KeyboardType.Text)
            )
        }
    }
}

private data class FieldData(
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String,
    val keyboardType: KeyboardType? = null
)