package com.investrove.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

@Singleton
class TokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getToken(): String? {
        return dataStore.data.first()[PreferencesKeys.TOKEN_KEY]
    }

    fun getTokenSync(): String? {
        var token: String? = null
        runBlocking {
            token = getToken()
        }
        return token
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[PreferencesKeys.TOKEN_KEY] = token }
    }

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(PreferencesKeys.TOKEN_KEY)
            it.remove(PreferencesKeys.REFRESH_TOKEN_KEY)
        }
    }

    fun isTokenExpired(token: String): Boolean {
        val parts = token.split(".")
        if (parts.size != 3) return true

        val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
        val exp = JSONObject(payload).optLong("exp", 0)
        val now = System.currentTimeMillis() / 1000
        return now >= exp
    }
}
