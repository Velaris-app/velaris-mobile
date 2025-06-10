package com.investrove.ui.feature.portfolio

import com.investrove.data.model.InvestmentItem

data class PortfolioState(
    val items: List<InvestmentItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
