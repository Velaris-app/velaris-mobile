package com.velaris.mobile.ui.feature.insights

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.common.stats.cards.PerformanceChartCard
import com.velaris.mobile.ui.feature.insights.components.InsightsState
import com.velaris.mobile.ui.feature.insights.components.cards.CategoryDistributionCard
import com.velaris.mobile.ui.feature.insights.components.cards.CategoryTrendCard
import com.velaris.mobile.ui.feature.insights.components.cards.PerformanceMetricsCard
import com.velaris.mobile.ui.feature.insights.components.cards.TagsStatsCard
import com.velaris.mobile.ui.feature.insights.components.cards.TopPerformersCard
import com.velaris.mobile.ui.feature.insights.components.cards.TrendDiffCard
import com.velaris.mobile.ui.feature.insights.components.filter.FilterSection

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CompactTopBar(
                text = "Statistics",
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = if (showFilters) Icons.Default.Close else Icons.Default.FilterList,
                            contentDescription = if (showFilters) "Close filters" else "Show filters"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (showFilters) {
                FilterSection(
                    selectedDateRange = state.selectedDateRange,
                    availableCategories = state.categoryStats.map { it.categoryName },
                    selectedCategories = state.selectedCategories,
                    onCategoriesChange = viewModel::updateSelectedCategories,
                    modifier = Modifier.padding(16.dp)
                )
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text(state.error!!) }
                }
                else -> InsightsCards(state)
            }
        }
    }
}

@Composable
private fun InsightsCards(state: InsightsState) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PerformanceChartCard(performanceData = state.trendStats)
        CategoryDistributionCard(categoryDistribution = state.categoryStats)
        PerformanceMetricsCard(overview = state.overviewStats)
        TopPerformersCard(topPerformers = state.topMoversStats)
        TrendDiffCard(trendDiff = state.trendDiffStats)
        CategoryTrendCard(categoryTrend = state.categoryTrendStats)
        TagsStatsCard(tags = state.tagStats)
    }
}
