package com.investrove.ui.feature.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.data.model.InvestmentItem
import com.investrove.domain.usecase.AddCollectibleItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddItemViewModel @Inject constructor(private val addItemUseCase: AddCollectibleItemUseCase) :
        ViewModel() {

    fun addItem(item: InvestmentItem, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                addItemUseCase(item)
                onComplete()
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}
