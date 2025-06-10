package com.investrove.ui.feature.additem.components.form

data class AddItemFormState(
    val name: String = "",
    val category: String = "",
    val emotionalValue: Int = 3,
    val purchasePrice: String = "",
    val currentValue: String = "",
    val acquisitionDate: String = "",
    val location: String = "",
    val currency: String = "PLN"
)

val categories = listOf(
    "Mieszkanie", "Złoto", "Waluta", "Akcje", "Fundusz ETF", "Kryptowaluty",
    "Figurka", "LEGO", "Karta Kolekcjonerska"
)

val currencies = listOf("PLN", "USD", "EUR", "GBP", "CHF")
