package com.velaris.mobile.ui.feature.insights.components

import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.domain.model.CategoryTrendStats
import com.velaris.mobile.domain.model.OverviewStats
import com.velaris.mobile.domain.model.TagStats
import com.velaris.mobile.domain.model.TopMoversStats
import com.velaris.mobile.domain.model.TrendDiffStats
import com.velaris.mobile.domain.model.TrendStats
import java.time.LocalDate

data class InsightsState(
    val selectedDateRange: DateRange = DateRange(
        start = LocalDate.now().minusMonths(1),
        end = LocalDate.now()
    ),
    val selectedCategories: Set<String> = emptySet(),
    val categoryStats: List<CategoryStats> = emptyList(),
    val trendStats: List<TrendStats> = emptyList(),
    val overviewStats: OverviewStats? = null,
    val tagStats: List<TagStats> = emptyList(),
    val trendDiffStats: List<TrendDiffStats> = emptyList(),
    val topMoversStats: List<TopMoversStats> = emptyList(),
    val categoryTrendStats: List<CategoryTrendStats> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)