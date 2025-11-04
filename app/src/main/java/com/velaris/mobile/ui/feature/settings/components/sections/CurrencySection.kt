package com.velaris.mobile.ui.feature.settings.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.velaris.mobile.ui.common.SearchableDropdownMenu
import com.velaris.mobile.ui.common.SectionCard
import com.velaris.mobile.ui.feature.auth.SessionViewModel
import java.util.Currency

@Composable
fun CurrencySection(viewModel: SessionViewModel) {
    val selectedCurrency by viewModel.userCurrency.collectAsState()
    val currencyOptions = Currency.getAvailableCurrencies().map { it.currencyCode }.sorted()

    SectionCard(title = "Currency") {
        SearchableDropdownMenu(
            options = currencyOptions,
            selectedOption = selectedCurrency,
            onOptionSelected = viewModel::updateCurrency
        )
    }
}
