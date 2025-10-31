package com.velaris.mobile.ui.feature.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.di.PreferencesKeys.CURRENCY_KEY
import com.velaris.mobile.di.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

sealed class SessionState {
    data object Loading : SessionState()
    data object LoggedIn : SessionState()
    data object LoggedOut : SessionState()
    data class Error(val message: String? = null) : SessionState()
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    // --- Session state ---
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    val isLoading: StateFlow<Boolean> = _sessionState
        .map { it is SessionState.Loading }
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // --- User currency ---
    private val _userCurrency =
        MutableStateFlow(Currency.getInstance(Locale.getDefault()).currencyCode)
    val userCurrency: StateFlow<String> = _userCurrency.asStateFlow()

    // --- Initialization ---
    init {
        viewModelScope.launch {
            loadUserCurrency()
            checkTokenValidity()
        }
    }

    // --- Currency handling ---
    private suspend fun loadUserCurrency() {
        val saved = dataStore.data
            .map { prefs -> prefs[CURRENCY_KEY] ?: "PLN" }
            .first()
        _userCurrency.value = saved
    }

    fun updateCurrency(newCurrency: String) {
        viewModelScope.launch {
            _userCurrency.value = newCurrency
            dataStore.edit { prefs -> prefs[CURRENCY_KEY] = newCurrency }
        }
    }

    // --- Session handling ---
    fun logout() {
        viewModelScope.launch {
            tokenProvider.clearTokens()
            _sessionState.value = SessionState.LoggedOut
        }
    }

    fun checkTokenValidity() {
        viewModelScope.launch {
            _sessionState.value = SessionState.Loading
            try {
                val token = tokenProvider.getAccessToken()

                if (token.isNullOrBlank()) {
                    _sessionState.value = SessionState.LoggedOut
                    return@launch
                }

                val expired = tokenProvider.isTokenExpired(token)

                if (expired) {
                    tokenProvider.clearTokens()
                    _sessionState.value = SessionState.LoggedOut
                } else {
                    _sessionState.value = SessionState.LoggedIn
                }
            } catch (e: Exception) {
                tokenProvider.clearTokens()
                _sessionState.value = SessionState.Error(e.message)
            }
        }
    }
}