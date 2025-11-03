package com.velaris.mobile.ui.feature.insights

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.common.stats.cards.PerformanceChartCard
import com.velaris.mobile.ui.feature.insights.components.InsightsState
import com.velaris.mobile.ui.feature.insights.components.cards.*
import com.velaris.mobile.ui.feature.insights.components.filter.FilterSection
import com.velaris.mobile.ui.navigation.BottomBarNavigation

@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
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
        },
        bottomBar = { BottomBarNavigation(navController) }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pullRefreshState,
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.loadAllStats() },
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isLoading,
                    state = pullRefreshState,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        ) {
            when {
                state.error != null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.error!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AnimatedVisibility(showFilters) {
                            FilterSection(
                                selectedDateRange = state.selectedDateRange,
                                availableCategories = state.categoryStats.map { it.categoryName },
                                selectedCategories = state.selectedCategories,
                                onCategoriesChange = { categories ->
                                    viewModel.updateSelectedCategories(categories)
                                    viewModel.loadAllStats()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        InsightsCards(state)
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightsCards(state: InsightsState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PerformanceChartCard(performanceData = state.trendStats)
        CategoryDistributionCard(categoryDistribution = state.categoryStats)
        PerformanceMetricsCard(overview = state.overviewStats)
        TopPerformersCard(topPerformers = state.topMoversStats)
        TrendDiffCard(trendDiff = state.trendDiffStats)
        CategoryTrendCard(categoryTrend = state.categoryTrendStats)
        TagsStatsCard(tags = state.tagStats)
    }
}