package com.investrove.domain.model

data class Collectible(
    val name: String,
    val category: String,
    val emotionalValue: Int,
    val imageUri: String? = null,
    val purchasePrice: Double? = null,
    val currentValue: Double? = null
)
