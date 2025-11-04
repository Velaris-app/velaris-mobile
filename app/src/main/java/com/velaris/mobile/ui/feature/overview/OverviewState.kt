package com.velaris.mobile.ui.feature.overview

import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.domain.model.OverviewStats
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.TrendStats

data class OverviewState(
    val overviewStats: OverviewStats = OverviewStats(
        totalAssets = 0,
        totalItems = 0,
        totalValue = java.math.BigDecimal.ZERO,
        currency = ""
    ),
    val performanceData: List<TrendStats> = emptyList(),
    val categories: List<CategoryStats> = emptyList(),
    val recentActivities: List<RecentActivity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
