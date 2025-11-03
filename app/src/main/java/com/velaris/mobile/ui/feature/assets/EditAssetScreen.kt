package com.velaris.mobile.ui.feature.assets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velaris.mobile.ui.feature.assets.components.cards.BasicInfoCard
import com.velaris.mobile.ui.feature.assets.components.cards.PriceConditionCard
import com.velaris.mobile.ui.feature.assets.components.cards.PriceConditionState
import com.velaris.mobile.ui.feature.assets.components.cards.QuantityTagsCard

@Composable
fun EditAssetScreen(
    assetId: Long,
    viewModel: AssetsViewModel,
    navController: NavController
) {
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val asset by viewModel.selectedAsset.collectAsState()

    LaunchedEffect(assetId) {
        viewModel.getAssetById(assetId)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            loading && asset == null -> {
                // pokaż loader dopóki asset się nie załadował
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            !loading && asset == null -> {
                // asset nie istnieje
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Asset not found", style = MaterialTheme.typography.bodyLarge)
                }
            }
            asset != null -> {
                val currentAsset = asset!!
                var name by remember { mutableStateOf(currentAsset.name) }
                var category by remember { mutableStateOf(currentAsset.category) }
                var description by remember { mutableStateOf(currentAsset.description ?: "") }
                var price by remember { mutableStateOf(currentAsset.purchasePrice) }
                var currency by remember { mutableStateOf(currentAsset.currency) }
                var condition by remember { mutableStateOf(currentAsset.condition ?: "") }
                var year by remember { mutableStateOf(currentAsset.year?.toString() ?: "") }
                var quantity by remember { mutableIntStateOf(currentAsset.quantity) }
                var tags by remember { mutableStateOf(currentAsset.tags.joinToString(", ")) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Edit Asset",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    BasicInfoCard(
                        name = name,
                        onNameChange = { name = it },
                        category = category,
                        onCategoryChange = { category = it },
                        description = description,
                        onDescriptionChange = { description = it }
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
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        tags = tags,
                        onTagsChange = { tags = it }
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
                            val updatedAsset = currentAsset.copy(
                                name = name,
                                category = category,
                                description = description,
                                purchasePrice = price,
                                currency = currency,
                                condition = condition,
                                year = year.toIntOrNull(),
                                quantity = quantity,
                                tags = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            )
                            viewModel.updateAsset(updatedAsset) {
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Update Asset", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}