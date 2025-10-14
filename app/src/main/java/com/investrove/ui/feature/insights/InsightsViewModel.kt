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
    val selectedDateRange: DateRange = DateRange(
        start = LocalDate.now().minusMonths(1),
        end = LocalDate.now()
    ),
    val selectedCategories: Set<String> = emptySet(),
    val portfolioStats: PortfolioStatsDto = PortfolioStatsDto(),
    val isLoading: Boolean = false,
    val error: String? = null
)

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
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val range = _state.value.selectedDateRange
                val stats = portfolioStatsRepository.getPortfolioStats(range.start, range.end)
                _state.update {
                    it.copy(
                        isLoading = false,
                        portfolioStats = stats,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}