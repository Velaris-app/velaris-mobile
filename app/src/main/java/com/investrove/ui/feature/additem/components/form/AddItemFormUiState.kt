package com.investrove.ui.feature.additem.components.form

data class AddItemFormUiState(
    val formState: AddItemFormState = AddItemFormState(),
    val categoryExpanded: Boolean = false,
    val currencyExpanded: Boolean = false,
    val showErrors: Boolean = false
)