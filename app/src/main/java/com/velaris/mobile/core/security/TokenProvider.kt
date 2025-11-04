package com.velaris.mobile.core.security

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.velaris.mobile.core.datastore.PreferencesKeys
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getAccessToken(): String? =
        dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN_KEY]

    suspend fun getRefreshToken(): String? =
        dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN_KEY]

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.REFRESH_TOKEN_KEY] = token
        }
    }

    suspend fun clearTokens() {
        dataStore.edit { prefs ->
            prefs.remove(PreferencesKeys.ACCESS_TOKEN_KEY)
            prefs.remove(PreferencesKeys.REFRESH_TOKEN_KEY)
        }
    }

    fun isTokenExpired(token: String?): Boolean {
        if (token.isNullOrBlank()) return true
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payloadJson = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP)
            )

            val exp = JSONObject(payloadJson).optLong("exp", 0L)
            if (exp <= 0L) return true

            val currentTime = System.currentTimeMillis() / 1000
            currentTime >= exp
        } catch (_: Exception) {
            true
        }
    }
}