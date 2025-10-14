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
    val investmentType: String? = null,

    val emotionalValue: Int = 0,

    val purchasePrice: Double? = null,
    val currentValue: Double? = null,
    val acquisitionDate: LocalDate? = null,

    val location: String? = null,
    val currency: String = "PLN",
    val imageUri: String? = null,

    val notes: String? = null,
    val condition: String? = null
)