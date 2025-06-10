package com.investrove.ui.feature.additem

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.investrove.data.model.InvestmentItem
import com.investrove.ui.feature.additem.components.form.AddItemForm
import com.investrove.ui.feature.additem.components.form.AddItemFormUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (_: DateTimeParseException) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavController, viewModel: AddItemViewModel) {
    var uiState by remember { mutableStateOf(AddItemFormUiState()) }
    val isFormValid = uiState.formState.name.isNotBlank() && uiState.formState.category.isNotBlank()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add New Investment") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    uiState = uiState.copy(showErrors = true)

                    if (isFormValid) {
                        val form = uiState.formState
                        val item = InvestmentItem(
                            name = form.name.trim(),
                            category = form.category,
                            emotionalValue = form.emotionalValue,
                            purchasePrice = form.purchasePrice.toDoubleOrNull(),
                            currentValue = form.currentValue.toDoubleOrNull(),
                            acquisitionDate = form.acquisitionDate.toLocalDateOrNull(),
                            location = form.location.ifBlank { null },
                            currency = form.currency,
                            imageUri = null
                        )
                        viewModel.addItem(item) {
                            navController.popBackStack()
                        }
                    }
                },
                containerColor = if (isFormValid)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        AddItemForm(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onStateChange = { newState -> uiState = uiState.copy(formState = newState) }
        )
    }
}