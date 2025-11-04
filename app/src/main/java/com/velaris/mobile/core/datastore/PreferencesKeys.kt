package com.velaris.mobile.core.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("token_key")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
    val CURRENCY_KEY = stringPreferencesKey("currency_key")
}