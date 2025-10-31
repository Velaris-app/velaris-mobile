package com.velaris.mobile.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.common.stats.cards.PerformanceChartCard
import com.velaris.mobile.ui.feature.overview.components.cards.InvestmentCategoriesCard
import com.velaris.mobile.ui.feature.overview.components.cards.PortfolioDistributionCard
import com.velaris.mobile.ui.feature.overview.components.cards.RecentActivityCard
import com.velaris.mobile.ui.feature.overview.components.cards.TotalValueCard

@Composable
fun OverviewScreen(viewModel: OverviewViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(topBar = { CompactTopBar("Overview") }) { padding ->
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TotalValueCard(
                    totalValue = state.totalValue,
                    currency = state.currency,
                    modifier = Modifier.padding(16.dp)
                )

                PortfolioDistributionCard(
                    distribution = state.portfolioDistribution,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                PerformanceChartCard(
                    performanceData = state.performanceData,
                    modifier = Modifier.padding(16.dp)
                )

                InvestmentCategoriesCard(
                    categories = state.categories,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                RecentActivityCard(
                    activities = state.recentActivities,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}