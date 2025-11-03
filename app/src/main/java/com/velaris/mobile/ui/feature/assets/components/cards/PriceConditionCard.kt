package com.velaris.mobile.ui.feature.assets.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.math.BigDecimal

data class PriceConditionState(
    val price: BigDecimal,
    val currency: String,
    val condition: String,
    val year: String,
    val onPriceChange: (BigDecimal) -> Unit,
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.price.toPlainString(),
                    onValueChange = { newValue ->
                        newValue.toBigDecimalOrNull()?.let { state.onPriceChange(it) }
                    },
                    label = { Text("Price") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = state.currency,
                    onValueChange = state.onCurrencyChange,
                    label = { Text("Currency") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Drugi wiersz: Condition i Year
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.condition,
                    onValueChange = state.onConditionChange,
                    label = { Text("Condition") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.year,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) state.onYearChange(newValue)
                    },
                    label = { Text("Year") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
private fun RowFields(fields: List<FieldData>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        fields.forEach { field ->
            OutlinedTextField(
                value = field.value,
                onValueChange = { newValue ->
                    if (field.isNumber) {
                        newValue.toBigDecimalOrNull()?.let { field.onValueChange(it.toString()) }
                    } else {
                        field.onValueChange(newValue)
                    }
                },
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
    val keyboardType: KeyboardType? = null,
    val isNumber: Boolean = false
)