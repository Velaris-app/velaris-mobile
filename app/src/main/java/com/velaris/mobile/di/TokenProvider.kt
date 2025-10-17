package com.velaris.mobile.di

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    suspend fun getToken(): String? {
        return dataStore.data.first()[PreferencesKeys.TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[PreferencesKeys.TOKEN_KEY] = token }
        _isLoggedIn.value = true
    }

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(PreferencesKeys.TOKEN_KEY)
            it.remove(PreferencesKeys.REFRESH_TOKEN_KEY)
        }
        _isLoggedIn.emit(false)
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val exp = JSONObject(payload).optLong("exp", 0)
            val now = System.currentTimeMillis() / 1000
            now >= exp
        } catch (_: Exception) {
            true
        }
    }
}
