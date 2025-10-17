package com.velaris.mobile.ui.feature.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.AssetsApi
import com.velaris.api.client.model.Asset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(
    private val assetsApi: AssetsApi
) : ViewModel() {

    private val _assets = MutableStateFlow<List<Asset>>(emptyList())
    val assets: StateFlow<List<Asset>> = _assets

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadAssets() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = assetsApi.listAssets()
                if (response.isSuccessful) {
                    _assets.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Failed to load assets: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteAsset(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = assetsApi.deleteAsset(id.toString())
                if (response.isSuccessful) {
                    _assets.value = _assets.value.filterNot { it.id == id }
                } else {
                    _error.value = "Failed to delete asset: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addAsset(asset: Asset, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = assetsApi.addAsset(asset)
                if (response.isSuccessful) {
                    _assets.value = _assets.value + listOf(response.body()!!)
                    onSuccess()
                } else {
                    _error.value = "Failed to add asset: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
