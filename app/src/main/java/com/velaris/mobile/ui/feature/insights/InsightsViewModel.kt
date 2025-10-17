package com.velaris.mobile.ui.feature.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.data.repository.StatsRepository
import com.velaris.mobile.ui.feature.insights.components.InsightsState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InsightsState())
    val state: StateFlow<InsightsState> = _state.asStateFlow()

    init {
        loadAllStats()
    }

    fun updateSelectedCategories(categories: Set<String>) {
        _state.update { it.copy(selectedCategories = categories) }
        loadAllStats()
    }

    private fun loadAllStats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val categoryStats = statsRepository.getCategoryStats()
                val trendStats = statsRepository.getTrendStats()
                val overviewStats = statsRepository.getOverviewStats()
                val tagStats = statsRepository.getTagStats()
                val trendDiffStats = statsRepository.getTrendDiffStats()
                val topMoversStats = statsRepository.getTopMovers()

                val selectedCategories = _state.value.selectedCategories.ifEmpty {
                    categoryStats.map { it.categoryName }.toSet()
                }

                val categoryTrendStats = selectedCategories.flatMap { category ->
                    statsRepository.getCategoryTrend(category)
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        categoryStats = categoryStats,
                        trendStats = trendStats,
                        overviewStats = overviewStats,
                        tagStats = tagStats,
                        trendDiffStats = trendDiffStats,
                        topMoversStats = topMoversStats,
                        categoryTrendStats = categoryTrendStats,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Unknown error") }
            }
        }
    }
}