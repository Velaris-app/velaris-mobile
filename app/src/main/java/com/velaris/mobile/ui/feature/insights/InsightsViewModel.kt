package com.velaris.mobile.ui.feature.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.model.*
import com.velaris.mobile.data.repository.StatsRepository
import com.velaris.mobile.core.util.ApiResult
import com.velaris.mobile.core.util.ErrorMapper
import com.velaris.mobile.ui.feature.insights.components.InsightsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun loadAllStats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val categoryStats = statsRepository.getCategoryStats()
            val trendStats = statsRepository.getTrendStats(TrendRequest(period = Period.WEEK))
            val overviewStats = statsRepository.getOverviewStats()
            val tagStats = statsRepository.getTagStats()
            val trendDiffStats = statsRepository.getTrendDiffStats()
            val topMoversStats = statsRepository.getTopMovers(
                SearchFilter(
                    paginationRequest = PaginationRequest(page = 0, propertySize = 20),
                    sortRequest = SortRequest(sortBy = "totalValue", sortDirection = SortDirection.DESC)
                )
            )

            val errorResult = listOf(
                categoryStats,
                trendStats,
                overviewStats,
                tagStats,
                trendDiffStats,
                topMoversStats
            ).filterIsInstance<ApiResult.Error>().firstOrNull()

            if (errorResult != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMapper.toUserMessage(errorResult.code, errorResult.message)
                    )
                }
                return@launch
            }

            val categoryData = (categoryStats as? ApiResult.Success)?.data.orEmpty()
            val trendData = (trendStats as? ApiResult.Success)?.data.orEmpty()
            val overviewData = (overviewStats as? ApiResult.Success)?.data
            val tagData = (tagStats as? ApiResult.Success)?.data.orEmpty()
            val trendDiffData = (trendDiffStats as? ApiResult.Success)?.data.orEmpty()
            val topMoversData = (topMoversStats as? ApiResult.Success)?.data.orEmpty()

            val selectedCategories = _state.value.selectedCategories.ifEmpty {
                categoryData.map { it.categoryName }.toSet()
            }

            val categoryTrendResults = selectedCategories.map { category ->
                statsRepository.getCategoryTrend(
                    CategoryTrendRequest(
                        category = category,
                        period = Period.WEEK
                    )
                )
            }

            val categoryTrendError = categoryTrendResults.filterIsInstance<ApiResult.Error>().firstOrNull()
            if (categoryTrendError != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMapper.toUserMessage(categoryTrendError.code, categoryTrendError.message)
                    )
                }
                return@launch
            }

            val categoryTrendData = categoryTrendResults.mapNotNull { result ->
                (result as? ApiResult.Success)?.data
            }.flatten()

            _state.update {
                it.copy(
                    isLoading = false,
                    categoryStats = categoryData,
                    trendStats = trendData,
                    overviewStats = overviewData,
                    tagStats = tagData,
                    trendDiffStats = trendDiffData,
                    topMoversStats = topMoversData,
                    categoryTrendStats = categoryTrendData,
                    error = null
                )
            }
        }
    }
}