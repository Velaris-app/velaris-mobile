package com.velaris.mobile.ui.feature.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.data.repository.AssetRepository
import com.velaris.mobile.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
                val overview = statsRepository.getOverviewStats()
                val categories = statsRepository.getCategoryStats()
                val performance = statsRepository.getTrendStats()
                val recent = assetsRepository.getRecentActivities()

                _state.update {
                    it.copy(
                        totalValue = overview.totalValue,
                        currency = overview.currency,
                        portfolioDistribution = categories,
                        performanceData = performance,
                        categories = categories,
                        recentActivities = recent,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}
