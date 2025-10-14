package com.investrove.ui.feature.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.api.client.model.Asset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(viewModel: AssetsViewModel, navController: NavController) {
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("USD") }
    var condition by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Add New Asset",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            BasicInfoCard(
                name = name, onNameChange = { name = it },
                category = category, onCategoryChange = { category = it },
                description = description, onDescriptionChange = { description = it }
            )

            PriceConditionCard(
                price = price, onPriceChange = { price = it },
                currency = currency, onCurrencyChange = { currency = it },
                condition = condition, onConditionChange = { condition = it },
                year = year, onYearChange = { year = it }
            )

            QuantityTagsCard(
                quantity = quantity, onQuantityChange = { quantity = it },
                tags = tags, onTagsChange = { tags = it }
            )

            if (!error.isNullOrEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = error ?: "",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Button(
                onClick = {
                    val asset = Asset(
                        name = name,
                        category = category,
                        description = description,
                        purchasePrice = price.toBigDecimalOrNull(),
                        currency = currency,
                        condition = condition,
                        year = year.toIntOrNull(),
                        quantity = quantity.toIntOrNull(),
                        tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    )
                    viewModel.addAsset(asset) { navController.popBackStack() }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Asset", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun BasicInfoCard(
    name: String, onNameChange: (String) -> Unit,
    category: String, onCategoryChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Basic Info", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = name, onValueChange = onNameChange, label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = category, onValueChange = onCategoryChange, label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = description, onValueChange = onDescriptionChange, label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
    }
}

@Composable
private fun PriceConditionCard(
    price: String, onPriceChange: (String) -> Unit,
    currency: String, onCurrencyChange: (String) -> Unit,
    condition: String, onConditionChange: (String) -> Unit,
    year: String, onYearChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Price & Condition", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = price, onValueChange = onPriceChange, label = { Text("Price") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = currency, onValueChange = onCurrencyChange, label = { Text("Currency") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = condition, onValueChange = onConditionChange, label = { Text("Condition") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = year, onValueChange = onYearChange, label = { Text("Year") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}

@Composable
private fun QuantityTagsCard(
    quantity: String, onQuantityChange: (String) -> Unit,
    tags: String, onTagsChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Quantity & Tags", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = quantity, onValueChange = onQuantityChange, label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = tags, onValueChange = onTagsChange, label = { Text("Tags (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}