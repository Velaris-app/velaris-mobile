package com.investrove.ui.feature.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.data.model.*
import com.investrove.data.repository.DateRange
import com.investrove.data.repository.PortfolioStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class InsightsState(
        val selectedDateRange: DateRange =
                DateRange(start = LocalDate.now().minusMonths(1), end = LocalDate.now()),
        val selectedCategories: Set<String> = emptySet(),
        val portfolioStats: PortfolioStats = PortfolioStats(),
        val isLoading: Boolean = false,
        val error: String? = null
)

data class PortfolioValuePoint(val date: LocalDate, val value: Double)

data class PerformanceMetrics(
        val totalReturn: Double = 0.0,
        val monthlyReturn: Double = 0.0,
        val bestPerformer: String = "",
        val worstPerformer: String = ""
)

data class InvestmentTypeData(val alternative: Double = 0.0, val traditional: Double = 0.0)

data class TopPerformer(val name: String, val category: String, val returnPercentage: Double)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val portfolioStatsRepository: PortfolioStatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InsightsState())
    val state: StateFlow<InsightsState> = _state.asStateFlow()

    init {
        loadInsightsData()
    }

    fun updateDateRange(range: DateRange) {
        _state.update { it.copy(selectedDateRange = range) }
        loadInsightsData()
    }

    fun updateSelectedCategories(categories: Set<String>) {
        _state.update { it.copy(selectedCategories = categories) }
        loadInsightsData()
    }

    private fun loadInsightsData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val stats = portfolioStatsRepository.getPortfolioStats(_state.value.selectedDateRange)
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        portfolioStats = stats,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                ) }
            }
        }
    }

    private fun generatePortfolioValueData(items: List<InvestmentItem>): List<PortfolioValuePoint> {
        // This would be replaced with real historical data in a production app
        return List(30) { i ->
            PortfolioValuePoint(
                    date = LocalDate.now().minusDays(29L - i),
                    value = items.sumOf { it.currentValue ?: 0.0 } * (1 + (i * 0.01))
            )
        }
    }

    private fun calculateCategoryDistribution(items: List<InvestmentItem>): Map<String, Double> {
        return items.groupBy { it.category }.mapValues { (_, items) ->
            items.sumOf { it.currentValue ?: 0.0 }
        }
    }

    private fun calculatePerformanceMetrics(items: List<InvestmentItem>): PerformanceMetrics {
        val totalValue = items.sumOf { it.currentValue ?: 0.0 }
        val purchaseValue = items.sumOf { it.purchasePrice ?: 0.0 }
        val totalReturn =
                if (purchaseValue > 0) ((totalValue - purchaseValue) / purchaseValue) * 100 else 0.0

        val bestPerformer =
                items.maxByOrNull { (it.currentValue ?: 0.0) - (it.purchasePrice ?: 0.0) }
        val worstPerformer =
                items.minByOrNull { (it.currentValue ?: 0.0) - (it.purchasePrice ?: 0.0) }

        return PerformanceMetrics(
                totalReturn = totalReturn,
                monthlyReturn = totalReturn / 12, // Simplified calculation
                bestPerformer = bestPerformer?.name ?: "",
                worstPerformer = worstPerformer?.name ?: ""
        )
    }

    private fun calculateInvestmentTypeData(items: List<InvestmentItem>): InvestmentTypeData {
        val alternativeCategories =
                setOf("Złoto", "Kryptowaluty", "Figurka", "LEGO", "Karta Kolekcjonerska")
        val traditionalCategories = setOf("Mieszkanie", "Akcje", "Fundusz ETF", "Waluta")

        val alternativeValue =
                items.filter { it.category in alternativeCategories }.sumOf {
                    it.currentValue ?: 0.0
                }

        val traditionalValue =
                items.filter { it.category in traditionalCategories }.sumOf {
                    it.currentValue ?: 0.0
                }

        return InvestmentTypeData(alternative = alternativeValue, traditional = traditionalValue)
    }

    private fun getTopPerformers(items: List<InvestmentItem>): List<TopPerformer> {
        return items
                .filter { it.purchasePrice != null && it.currentValue != null }
                .map { item ->
                    val returnPercentage =
                            ((item.currentValue!! - item.purchasePrice!!) / item.purchasePrice!!) *
                                    100
                    TopPerformer(
                            name = item.name,
                            category = item.category,
                            returnPercentage = returnPercentage
                    )
                }
                .sortedByDescending { it.returnPercentage }
                .take(5)
    }
}
