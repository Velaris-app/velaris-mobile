package com.velaris.mobile.ui.feature.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.model.*
import com.velaris.mobile.data.repository.StatsRepository
import com.velaris.mobile.core.util.ApiResult
import com.velaris.mobile.core.util.ErrorMapper
import com.velaris.mobile.core.util.dataOrEmpty
import com.velaris.mobile.core.util.dataOrNull
import com.velaris.mobile.ui.feature.insights.components.InsightsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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

            try {
                val categoryStatsDeferred = async { statsRepository.getCategoryStats() }
                val trendStatsDeferred = async { statsRepository.getTrendStats(TrendRequest(period = Period.WEEK)) }
                val overviewStatsDeferred = async { statsRepository.getOverviewStats() }
                val tagStatsDeferred = async { statsRepository.getTagStats() }
                val trendDiffStatsDeferred = async { statsRepository.getTrendDiffStats() }
                val topMoversStatsDeferred = async {
                    statsRepository.getTopMovers(
                        SearchFilter(
                            paginationRequest = PaginationRequest(page = 0, propertySize = 20),
                            sortRequest = SortRequest(sortBy = "totalValue", sortDirection = SortDirection.DESC)
                        )
                    )
                }

                val categoryStats = categoryStatsDeferred.await()
                val trendStats = trendStatsDeferred.await()
                val overviewStats = overviewStatsDeferred.await()
                val tagStats = tagStatsDeferred.await()
                val trendDiffStats = trendDiffStatsDeferred.await()
                val topMoversStats = topMoversStatsDeferred.await()

                val allResults = listOf(categoryStats, trendStats, overviewStats, tagStats, trendDiffStats, topMoversStats)
                val errorResult = allResults.filterIsInstance<ApiResult.Error>().firstOrNull()
                if (errorResult != null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = ErrorMapper.toUserMessage(errorResult.code, errorResult.message)
                        )
                    }
                    return@launch
                }

                val categoryData = categoryStats.dataOrEmpty()
                val trendData = trendStats.dataOrEmpty()
                val overviewData = overviewStats.dataOrNull()
                val tagData = tagStats.dataOrEmpty()
                val trendDiffData = trendDiffStats.dataOrEmpty()
                val topMoversData = topMoversStats.dataOrEmpty()

                val selectedCategories = _state.value.selectedCategories.ifEmpty {
                    categoryData.map { it.categoryName }.toSet()
                }

                val categoryTrendResults = selectedCategories.map { category ->
                    async { statsRepository.getCategoryTrend(
                        CategoryTrendRequest(category = category, period = Period.WEEK)) }
                }.map { it.await() }

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

                val categoryTrendData = categoryTrendResults.mapNotNull { it.dataOrNull() }.flatten()

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
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}