package com.investrove.ui.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.data.repository.InvestmentItemRepository
import com.investrove.domain.usecase.GetPortfolioStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OverviewState(
        val totalValue: Double = 0.0,
        val currency: String = "PLN",
        val portfolioDistribution: Map<String, Double> = emptyMap(),
        val performanceData: List<PerformanceDataPoint> = emptyList(),
        val categories: List<CategoryStats> = emptyList(),
        val recentActivities: List<ActivityItem> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
)

data class PerformanceDataPoint(val date: Long, val value: Double)

data class CategoryStats(val name: String, val value: Double, val percentage: Double)

data class ActivityItem(
        val type: String,
        val description: String,
        val date: Long,
        val value: Double
)

@HiltViewModel
class OverviewViewModel
@Inject
constructor(
        private val repository: InvestmentItemRepository,
        private val getPortfolioStatsUseCase: GetPortfolioStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OverviewState(isLoading = true))
    val state: StateFlow<OverviewState> = _state.asStateFlow()

    init {
        loadOverviewData()
    }

    fun refresh() {
        loadOverviewData()
    }

    private fun loadOverviewData() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)

                // Get portfolio stats
                val stats = getPortfolioStatsUseCase()

                // Calculate portfolio distribution
                val items = repository.getAllCollectibles()
                val distribution =
                        items.groupBy { it.category }.mapValues { (_, items) ->
                            items.sumOf { it.currentValue ?: 0.0 }
                        }

                // Calculate category stats
                val categories =
                    if (stats.totalEstimatedValue != 0.0) {
                        distribution.map { (name, value) ->
                            CategoryStats(
                                name = name,
                                value = value,
                                percentage = (value / stats.totalEstimatedValue) * 100
                            )
                        }
                    } else {
                        distribution.map { (name, value) ->
                            CategoryStats(name = name, value = value, percentage = 0.0)
                        }
                    }.sortedByDescending { it.value }

                // Generate performance data (mock data for now)
                val performanceData = generateMockPerformanceData()

                // Generate recent activities (mock data for now)
                val activities = generateMockActivities()

                _state.value =
                        _state.value.copy(
                                totalValue = stats.totalEstimatedValue,
                                portfolioDistribution = distribution,
                                performanceData = performanceData,
                                categories = categories,
                                recentActivities = activities,
                                isLoading = false
                        )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    private fun generateMockPerformanceData(): List<PerformanceDataPoint> {
        // This would be replaced with real data in a production app
        return List(30) { i ->
                    PerformanceDataPoint(
                            date = System.currentTimeMillis() - (i * 24 * 60 * 60 * 1000),
                            value = 1000.0 + (i * 10.0)
                    )
                }
                .reversed()
    }

    private fun generateMockActivities(): List<ActivityItem> {
        // This would be replaced with real data in a production app
        return List(5) { i ->
            ActivityItem(
                    type = if (i % 2 == 0) "Purchase" else "Sale",
                    description = "Investment ${i + 1}",
                    date = System.currentTimeMillis() - (i * 24 * 60 * 60 * 1000),
                    value = 1000.0 + (i * 100.0)
            )
        }
    }
}
