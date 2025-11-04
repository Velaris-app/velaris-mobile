package com.velaris.mobile.ui.feature.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.data.repository.AssetRepository
import com.velaris.mobile.core.util.dataOrEmpty
import com.velaris.mobile.core.util.dataOrNull
import com.velaris.mobile.domain.model.AssetItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val repository: AssetRepository
) : ViewModel() {

    private val _assets = MutableStateFlow<List<AssetItem>>(emptyList())
    val assets: StateFlow<List<AssetItem>> = _assets

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedAsset = MutableStateFlow<AssetItem?>(null)
    val selectedAsset: StateFlow<AssetItem?> = _selectedAsset

    init { loadAssets() }

    fun loadAssets() = launchWithLoading {
        _assets.value = repository.getAssets().dataOrEmpty()
    }

    fun getAssetById(id: Long) = launchWithLoading {
        _selectedAsset.value = repository.getAsset(id).dataOrNull()
    }

    fun addAsset(asset: AssetItem, onSuccess: () -> Unit) = launchWithLoading {
        val added = repository.addAsset(asset).dataOrNull()
            ?: throw Exception("No asset returned from API")
        _assets.value = _assets.value + added
        onSuccess()
    }

    fun updateAsset(asset: AssetItem, onSuccess: () -> Unit) {
        if (asset.id == null) {
            _error.value = "Asset ID is missing"
            return
        }
        launchWithLoading {
            val updated = repository.updateAsset(asset.id, asset).dataOrNull()
                ?: throw Exception("No asset returned from API")
            _assets.value = _assets.value.map { if (it.id == updated.id) updated else it }
            onSuccess()
        }
    }

    fun deleteAsset(id: Long) = launchWithLoading {
        repository.deleteAsset(id)
        _assets.value = _assets.value.filterNot { it.id == id }
    }

    private fun launchWithLoading(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            _loading.value = true
            _error.value = null
            block()
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unexpected error"
        } finally {
            _loading.value = false
        }
    }
}
