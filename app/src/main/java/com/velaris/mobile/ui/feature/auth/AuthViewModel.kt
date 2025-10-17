package com.velaris.mobile.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velaris.mobile.di.TokenProvider
import com.velaris.api.client.AuthApi
import com.velaris.api.client.model.LoginRequest
import com.velaris.api.client.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.token?.let {
                    tokenProvider.saveToken(it)
                    onSuccess()
                }
            } else {
                _error.value = "Login error: ${response.code()}"
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.register(RegisterRequest(email, password, username))
            if (response.isSuccessful) onSuccess()
            else _error.value = "Register error ${response.code()}"
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
