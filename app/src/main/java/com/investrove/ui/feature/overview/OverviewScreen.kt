package com.investrove.ui.feature.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.investrove.ui.feature.overview.components.cards.InvestmentCategoriesCard
import com.investrove.ui.feature.overview.components.cards.PerformanceChartCard
import com.investrove.ui.feature.overview.components.cards.PortfolioDistributionCard
import com.investrove.ui.feature.overview.components.cards.RecentActivityCard
import com.investrove.ui.feature.overview.components.cards.TotalValueCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(viewModel: OverviewViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value

    Scaffold(topBar = { TopAppBar(title = { Text("Portfolio Overview") }) }) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(padding)
                                .verticalScroll(rememberScrollState())
        ) {
            // Total Value Card
            TotalValueCard(
                totalValue = state.totalValue,
                currency = state.currency,
                modifier = Modifier.padding(16.dp)
            )

            // Portfolio Distribution
            PortfolioDistributionCard(
                distribution = state.portfolioDistribution,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Performance Chart
            PerformanceChartCard(
                performanceData = state.performanceData,
                modifier = Modifier.padding(16.dp)
            )

            // Investment Categories
            InvestmentCategoriesCard(
                categories = state.categories,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Recent Activity
            RecentActivityCard(
                activities = state.recentActivities,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
