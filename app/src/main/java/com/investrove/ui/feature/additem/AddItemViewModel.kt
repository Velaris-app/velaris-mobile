package com.investrove.ui.feature.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.data.model.InvestmentItem
import com.investrove.data.repository.InvestmentItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: InvestmentItemRepository
) : ViewModel() {

    fun addItem(item: InvestmentItem, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insert(item)
            onComplete()
        }
    }
}
