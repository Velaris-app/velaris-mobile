package com.velaris.mobile.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.api.client.AuthApi
import com.velaris.api.client.model.GrantType
import com.velaris.api.client.model.TokenRequest
import com.velaris.api.client.model.RegisterRequest
import com.velaris.mobile.core.di.TokenProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun login(username: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.getToken(
                TokenRequest(
                    grantType = GrantType.PASSWORD,
                    username = username,
                    password = password
                )
            )
            if (response.isSuccessful) {
                response.body()?.let { tokenResponse ->
                    tokenProvider.saveAccessToken(tokenResponse.accessToken ?: "")
                    tokenProvider.saveRefreshToken(tokenResponse.refreshToken ?: "")
                    onSuccess()
                } ?: run { _error.value = "Empty response from server" }
            } else {
                _error.value = "Login error: ${response.code()}"
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.registerUser(RegisterRequest(email = email, password = password, username = username))
            if (response.isSuccessful) {
                onSuccess()
            } else {
                _error.value = "Register error: ${response.code()}"
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
