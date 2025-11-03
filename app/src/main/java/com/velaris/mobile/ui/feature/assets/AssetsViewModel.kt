package com.velaris.mobile.ui.feature.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.data.repository.AssetRepository
import com.velaris.mobile.data.util.ApiResult
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

    init {
        loadAssets()
    }

    fun loadAssets() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.getAssets()) {
                is ApiResult.Success -> _assets.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun getAssetById(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.getAsset(id)) {
                is ApiResult.Success -> _selectedAsset.value = result.data
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun addAsset(asset: AssetItem, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.addAsset(asset)) {
                is ApiResult.Success -> {
                    _assets.value = _assets.value + result.data
                    onSuccess()
                }
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun updateAsset(asset: AssetItem, onSuccess: () -> Unit) {
        if (asset.id == null) {
            _error.value = "Asset ID is missing"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.updateAsset(asset.id, asset)) {
                is ApiResult.Success -> {
                    val updated = result.data
                    _assets.value = _assets.value.map { if (it.id == updated.id) updated else it }
                    onSuccess()
                }
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }

    fun deleteAsset(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            when (val result = repository.deleteAsset(id.toString())) {
                is ApiResult.Success -> _assets.value = _assets.value.filterNot { it.id == id }
                is ApiResult.Error -> _error.value = result.message
            }
            _loading.value = false
        }
    }
}
