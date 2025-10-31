package com.velaris.mobile.ui.feature.assets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.api.client.model.Asset
import com.velaris.mobile.ui.feature.assets.components.cards.BasicInfoCard
import com.velaris.mobile.ui.feature.assets.components.cards.PriceConditionCard
import com.velaris.mobile.ui.feature.assets.components.cards.PriceConditionState
import com.velaris.mobile.ui.feature.assets.components.cards.QuantityTagsCard

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
                state = PriceConditionState(
                    price = price,
                    onPriceChange = { price = it },
                    currency = currency,
                    onCurrencyChange = { currency = it },
                    condition = condition,
                    onConditionChange = { condition = it },
                    year = year,
                    onYearChange = { year = it }
                )
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