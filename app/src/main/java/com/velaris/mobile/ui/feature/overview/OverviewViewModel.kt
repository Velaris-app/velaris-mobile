package com.velaris.mobile.ui.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.model.Period
import com.velaris.api.client.model.TrendRequest
import com.velaris.mobile.data.repository.AssetRepository
import com.velaris.mobile.data.repository.StatsRepository
import com.velaris.mobile.core.util.ApiResult
import com.velaris.mobile.core.util.ErrorMapper
import com.velaris.mobile.core.util.dataOrEmpty
import com.velaris.mobile.core.util.dataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

            try {
                val overviewDeferred = async { statsRepository.getOverviewStats() }
                val categoriesDeferred = async { statsRepository.getCategoryStats() }
                val performanceDeferred = async { statsRepository.getTrendStats(TrendRequest(period = Period.WEEK)) }
                val recentDeferred = async { assetsRepository.getRecentActivities() }

                val overviewResult = overviewDeferred.await()
                val categoriesResult = categoriesDeferred.await()
                val performanceResult = performanceDeferred.await()
                val recentResult = recentDeferred.await()

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

                val overview = overviewResult.dataOrNull()
                val categories = categoriesResult.dataOrEmpty()
                val performance = performanceResult.dataOrEmpty()
                val recent = recentResult.dataOrEmpty()

                _state.update {
                    it.copy(
                        overviewStats = overview!!,
                        performanceData = performance,
                        categories = categories,
                        recentActivities = recent,
                        isLoading = false,
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