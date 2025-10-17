package com.velaris.mobile.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.velaris.mobile.ui.common.CompactTopBar
import com.velaris.mobile.ui.feature.overview.components.cards.InvestmentCategoriesCard
import com.velaris.mobile.ui.feature.overview.components.cards.PerformanceChartCard
import com.velaris.mobile.ui.feature.overview.components.cards.PortfolioDistributionCard
import com.velaris.mobile.ui.feature.overview.components.cards.RecentActivityCard
import com.velaris.mobile.ui.feature.overview.components.cards.TotalValueCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OverviewScreen(viewModel: OverviewViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            viewModel.loadOverview()
        }
    )

    Scaffold(topBar = { CompactTopBar("Overview") }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState)
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

            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}