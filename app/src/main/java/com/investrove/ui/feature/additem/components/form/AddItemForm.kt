package com.investrove.ui.feature.additem.components.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.investrove.ui.common.DatePickerField
import com.investrove.ui.common.SectionCard
import com.investrove.ui.common.dropdown.DropdownConfig
import com.investrove.ui.common.dropdown.GenericDropdown

@Composable
fun AddItemForm(
    modifier: Modifier = Modifier,
    uiState: AddItemFormUiState,
    onStateChange: (AddItemFormState) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionCard("Basic Info") {
            OutlinedTextField(
                value = uiState.formState.name,
                onValueChange = { onStateChange(uiState.formState.copy(name = it)) },
                label = { Text("Name") },
                isError = uiState.showErrors && uiState.formState.name.isBlank(),
                supportingText = {
                    if (uiState.showErrors && uiState.formState.name.isBlank()) Text("Name cannot be empty")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            GenericDropdown(
                value = uiState.formState.category,
                onValueSelected = { onStateChange(uiState.formState.copy(category = it)) },
                options = categories,
                config = DropdownConfig(
                    label = "Category",
                    isError = uiState.showErrors && uiState.formState.category.isBlank(),
                    errorMessage = "Category must be selected"
                )
            )
        }

        SectionCard("Emotional Value") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.SentimentDissatisfied, contentDescription = null)
                Slider(
                    value = uiState.formState.emotionalValue.toFloat(),
                    onValueChange = { onStateChange(uiState.formState.copy(emotionalValue = it.toInt())) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.SentimentSatisfied, contentDescription = null)
            }
        }

        SectionCard("Prices") {
            OutlinedTextField(
                value = uiState.formState.purchasePrice,
                onValueChange = {
                    val filtered = it.filter { ch -> ch.isDigit() || ch == '.' }
                    onStateChange(uiState.formState.copy(purchasePrice = filtered))
                },
                label = { Text("Purchase Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.formState.currentValue,
                onValueChange = {
                    val filtered = it.filter { ch -> ch.isDigit() || ch == '.' }
                    onStateChange(uiState.formState.copy(currentValue = filtered))
                },
                label = { Text("Current Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerField(
                label = "Acquisition Date",
                date = uiState.formState.acquisitionDate,
                onDateSelected = { onStateChange(uiState.formState.copy(acquisitionDate = it)) }
            )

            AnimatedVisibility(
                visible = uiState.formState.category == "Mieszkanie",
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                OutlinedTextField(
                    value = uiState.formState.location,
                    onValueChange = { onStateChange(uiState.formState.copy(location = it)) },
                    label = { Text("Location") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        SectionCard("Currency") {
            GenericDropdown(
                value = uiState.formState.currency,
                onValueSelected = { onStateChange(uiState.formState.copy(currency = it)) },
                options = currencies,
                config = DropdownConfig(
                    label = "Currency",
                    isError = uiState.showErrors && uiState.formState.currency.isBlank(),
                    errorMessage = "Currency must be selected"
                )
            )
        }
    }
}
