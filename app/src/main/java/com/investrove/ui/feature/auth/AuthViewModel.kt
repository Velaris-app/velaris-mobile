package com.investrove.ui.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.investrove.di.TokenProvider
import com.velaris.api.client.AuthApi
import com.velaris.api.client.model.LoginRequest
import com.velaris.api.client.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : ViewModel() {

    val error = MutableStateFlow<String?>(null)

    fun login(email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.token?.let {
                    tokenProvider.saveToken(it)
                    onSuccess()
                }
            } else {
                error.value = "Błąd logowania: ${response.code()}"
            }
        } catch (e: Exception) {
            error.value = e.message
        }
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) = viewModelScope.launch {
        try {
            val response = authApi.register(RegisterRequest(email, password, username))
            if (response.isSuccessful) onSuccess()
            else error.value = "Błąd rejestracji: ${response.code()}"
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}
