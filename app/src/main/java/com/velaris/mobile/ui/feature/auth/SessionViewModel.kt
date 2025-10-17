package com.velaris.mobile.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.di.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenProvider: TokenProvider
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = tokenProvider.isLoggedIn

    fun logout() {
        viewModelScope.launch {
            tokenProvider.clearTokens()
        }
    }

    fun checkTokenValidity() {
        viewModelScope.launch {
            val token = tokenProvider.getToken()
            if (token == null || tokenProvider.isTokenExpired(token)) {
                tokenProvider.clearTokens()
            }
        }
    }
}
