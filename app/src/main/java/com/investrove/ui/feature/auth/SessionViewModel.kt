package com.investrove.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.di.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        _isLoggedIn.value = !tokenProvider.getTokenSync().isNullOrEmpty()
    }

    private suspend fun updateLoginState() {
        val token = tokenProvider.getToken()
        _isLoggedIn.value = !token.isNullOrEmpty()
    }

    suspend fun refreshSession() {
        updateLoginState()
    }

    fun logout() {
        viewModelScope.launch {
            tokenProvider.clearTokens()
            _isLoggedIn.value = false
        }
    }
}
