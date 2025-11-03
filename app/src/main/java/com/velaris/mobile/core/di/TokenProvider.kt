package com.velaris.mobile.core.di

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getAccessToken(): String? {
        return dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN_KEY]
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { it[PreferencesKeys.ACCESS_TOKEN_KEY] = token }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { it[PreferencesKeys.REFRESH_TOKEN_KEY] = token }
    }

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(PreferencesKeys.ACCESS_TOKEN_KEY)
            it.remove(PreferencesKeys.REFRESH_TOKEN_KEY)
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val exp = JSONObject(payload).optLong("exp", 0)
            val now = System.currentTimeMillis() / 1000
            now >= exp
        } catch (_: Exception) {
            true
        }
    }
}