package com.velaris.mobile.ui.feature.overview

import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.TrendStats
import java.math.BigDecimal

data class OverviewState(
    val totalValue: BigDecimal = BigDecimal.ZERO,
    val currency: String = "PLN",
    val performanceData: List<TrendStats> = emptyList(),
    val categories: List<CategoryStats> = emptyList(),
    val recentActivities: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
