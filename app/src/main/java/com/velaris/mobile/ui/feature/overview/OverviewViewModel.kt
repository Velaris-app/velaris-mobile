package com.velaris.mobile.ui.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.model.Period
import com.velaris.api.client.model.TrendRequest
import com.velaris.mobile.data.repository.AssetRepository
import com.velaris.mobile.data.repository.StatsRepository
import com.velaris.mobile.data.util.ApiResult
import com.velaris.mobile.data.util.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val statsRepository: StatsRepository,
    private val assetsRepository: AssetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OverviewState())
    val state: StateFlow<OverviewState> = _state.asStateFlow()

    init {
        loadOverview()
    }

    fun loadOverview() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val overviewResult = statsRepository.getOverviewStats()
            val categoriesResult = statsRepository.getCategoryStats()
            val performanceResult = statsRepository.getTrendStats(TrendRequest(period = Period.WEEK))
            val recentResult = assetsRepository.getRecentActivities()

            val errorResult = listOf(overviewResult, categoriesResult, performanceResult, recentResult)
                .filterIsInstance<ApiResult.Error>()
                .firstOrNull()

            if (errorResult != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = ErrorMapper.toUserMessage(errorResult.code, errorResult.message)
                    )
                }
                return@launch
            }

            val overview = (overviewResult as? ApiResult.Success)?.data
            val categories = (categoriesResult as? ApiResult.Success)?.data.orEmpty()
            val performance = (performanceResult as? ApiResult.Success)?.data.orEmpty()
            val recent = (recentResult as? ApiResult.Success)?.data.orEmpty()

            _state.update {
                it.copy(
                    totalValue = overview?.totalValue ?: BigDecimal.ZERO,
                    currency = overview?.currency ?: "PLN",
                    portfolioDistribution = categories,
                    performanceData = performance,
                    categories = categories,
                    recentActivities = recent,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}