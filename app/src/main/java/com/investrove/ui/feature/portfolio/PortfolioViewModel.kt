package com.investrove.ui.feature.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.data.repository.InvestmentItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PortfolioViewModel @Inject constructor(private val repository: InvestmentItemRepository) :
        ViewModel() {

    private val _state = MutableStateFlow(PortfolioState(isLoading = true))
    val state: StateFlow<PortfolioState> = _state.asStateFlow()

    init {
        loadPortfolio()
    }

    fun refreshPortfolio() {
        loadPortfolio()
    }

    private fun loadPortfolio() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                val items = repository.getAllCollectibles()
                _state.value = _state.value.copy(items = items, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}
