package com.investrove.data.model

import androidx.room.Entity
import java.time.LocalDate


import androidx.room.PrimaryKey

@Entity(tableName = "investment_items")
data class InvestmentItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,
    val category: String,
    val emotionalValue: Int,
    val purchasePrice: Double?,
    val currentValue: Double?,
    val acquisitionDate: LocalDate?,
    val location: String?,
    val currency: String = "PLN",
    val imageUri: String? = null
)
