package com.velaris.mobile.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.common.stats.cards.PerformanceChartCard
import com.velaris.mobile.ui.feature.overview.components.cards.InvestmentCategoriesCard
import com.velaris.mobile.ui.feature.overview.components.cards.RecentActivityCard
import com.velaris.mobile.ui.feature.overview.components.cards.TotalValueCard
import com.velaris.mobile.ui.navigation.BottomBarNavigation

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = { CompactTopBar("Overview") },
        bottomBar = { BottomBarNavigation(navController) }
        ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.loadOverview() },
            state = pullRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isLoading,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullRefreshState
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    TotalValueCard(
                        overviewStats = state.overviewStats,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    PerformanceChartCard(
                        performanceData = state.performanceData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    InvestmentCategoriesCard(
                        categories = state.categories,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    RecentActivityCard(
                        activities = state.recentActivities,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}