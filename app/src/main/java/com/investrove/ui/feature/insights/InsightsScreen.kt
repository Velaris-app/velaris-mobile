package com.investrove.ui.feature.insights

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.investrove.ui.feature.insights.components.*
import com.investrove.ui.feature.insights.components.cards.InvestmentTypeComparison
import com.investrove.ui.feature.insights.components.cards.PerformanceMetricsCard
import com.investrove.ui.feature.insights.components.cards.TopPerformersCard
import com.investrove.ui.feature.insights.components.charts.CategoryDistributionChart
import com.investrove.ui.feature.insights.components.charts.PortfolioValueChart
import com.investrove.ui.feature.insights.components.filter.FilterSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statystyki") },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtry"
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
            AnimatedVisibility(
                visible = showFilters,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                FilterSection(
                    selectedDateRange = state.selectedDateRange,
                    selectedCategories = state.selectedCategories,
                    onDateRangeChange = viewModel::updateDateRange,
                    onCategoriesChange = viewModel::updateSelectedCategories,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(state.error!!)
                }
            } else {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PortfolioValueChart(
                        historicalData = state.portfolioStats.historicalData,
                        modifier = Modifier.fillMaxWidth()
                    )

                    CategoryDistributionChart(
                        categoryDistribution = state.portfolioStats.categoryDistribution,
                        modifier = Modifier.fillMaxWidth()
                    )

                    PerformanceMetricsCard(
                        portfolioStats = state.portfolioStats,
                        modifier = Modifier.fillMaxWidth()
                    )

                    InvestmentTypeComparison(
                        investmentTypeDistribution = state.portfolioStats.investmentTypeDistribution,
                        modifier = Modifier.fillMaxWidth()
                    )

                    TopPerformersCard(
                        topPerformers = state.portfolioStats.topPerformers,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
