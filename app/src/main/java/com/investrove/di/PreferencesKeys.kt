package com.investrove.di

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val TOKEN_KEY = stringPreferencesKey("token_key")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
}
